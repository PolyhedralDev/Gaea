package org.polydev.gaea.structures.features;

import org.polydev.gaea.structures.NMSStructure;

import java.util.Random;

public interface Feature {
    void populate(NMSStructure s, Random r);
}
