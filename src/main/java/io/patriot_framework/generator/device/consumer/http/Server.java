/*
 * Copyright 2021 Patriot project
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.patriot_framework.generator.device.consumer.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.patriot_framework.generator.Data;
import io.patriot_framework.generator.device.AbstractDevice;
import io.patriot_framework.generator.device.consumer.Consumer;
import io.patriot_framework.generator.device.consumer.ConsumerData;
import io.patriot_framework.generator.device.consumer.Storage;
import io.patriot_framework.generator.device.consumer.exceptions.ConsumerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.NotImplementedException;

import java.net.InetSocketAddress;
import java.nio.channels.UnresolvedAddressException;
import java.util.List;

public final class Server extends AbstractDevice implements Consumer {

    private final String serverHost;
    private final int serverPort;
    private final Storage storage;
    private SSLInit sslInit;
    private String[] suites = new String[]{};
    private ChannelFuture future;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    public static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public Server(String serverHost, int serverPort, Storage storage) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.storage = storage;
    }

    public Server(String serverHost, int serverPort, Storage storage, SSLInit sslInit) {
        this(serverHost, serverPort, storage);
        this.sslInit = sslInit;
    }

    public Server(String serverHost, int serverPort, Storage storage, SSLInit sslInit, String[] suites) {
        this(serverHost, serverPort, storage, sslInit);
        this.suites = suites;
    }

    @Override
    public void start() throws ConsumerException {
        if (bossGroup != null || workerGroup != null) {
            throw new ConsumerException("Server is already running");
        }
        try {
            LOGGER.info(String.format("Trying to start HTTP%s server [PORT: %d]", sslInit != null ? "S" : "", serverPort));

            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();

            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(serverHost, serverPort))
                    .childHandler(new ServerInitializer(getUUID(), storage, sslInit, suites))
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            future = bootstrap.bind().sync();
            LOGGER.info(String.format("Started HTTP%s server [PORT: %d]", sslInit != null ? "S" : "", serverPort));
        } catch (UnresolvedAddressException e) {
            throw new ConsumerException("Invalid hostname", e);
        } catch (InterruptedException e) {
            throw new ConsumerException("Server was interrupted", e);
        }
    }

    @Override
    public void stop() {
        try {
            if (workerGroup != null) {
                workerGroup.shutdownGracefully();
                workerGroup = null;
            }
            if (bossGroup != null) {
                bossGroup.shutdownGracefully();
                bossGroup = null;
            }
            if (future != null) {
                future.channel().closeFuture().sync();
                future = null;
                LOGGER.info(String.format("Stopped HTTP%s server [PORT: %d]", sslInit != null ? "S" : "", serverPort));
            } else {
                LOGGER.info(String.format("HTTP%s server was not running [PORT: %d]", sslInit != null ? "S" : "", serverPort));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ConsumerData> getContents() {
        return storage.getAll();
    }

    @Override
    public List<Data> requestData(Object... params) {
        throw new NotImplementedException("");
    }
}
