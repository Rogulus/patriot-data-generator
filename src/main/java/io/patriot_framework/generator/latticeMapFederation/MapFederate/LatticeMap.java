/*
 * Copyright 2024 Patriot project
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

package io.patriot_framework.generator.latticeMapFederation.MapFederate;

public class LatticeMap {
    private int[][] map;
    private int width;
    private int height;

    public LatticeMap(int width, int height) {
        map = new int[width][height];
        this.width = width;
        this.height = height;
    }

    public int getItem(int x, int y) {
        return map[x][y];
    }

    public void setItem(int item, int x, int y) {
        map[x][y] = item;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String toString() {
        String res = "";
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                res += map[x][y];
                res += " ";
            }
            res +="\n";
        }
        return res;
    }
}
