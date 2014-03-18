package com.mayorgraeme.evol.data.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 *
 * @author graeme
 */
public class SystemInfo {

    private final int maxX, maxY;
    private final List<ActorData>[][] data;

    public SystemInfo(int maxX, int maxY, List<ActorData>[][] data) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.data = data;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public List<ActorData>[][] getData() {
        return data;
    }
}
