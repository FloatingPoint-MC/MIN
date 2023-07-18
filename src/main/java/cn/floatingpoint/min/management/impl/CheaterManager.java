package cn.floatingpoint.min.management.impl;

import cn.floatingpoint.min.management.Manager;

import java.util.HashSet;
import java.util.UUID;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-18 11:41:57
 */
public class CheaterManager implements Manager {
    public HashSet<UUID> uuids;

    @Override
    public String getName() {
        return "Cheater Manager";
    }

    @Override
    public void init() {
        uuids = new HashSet<>();
    }
}
