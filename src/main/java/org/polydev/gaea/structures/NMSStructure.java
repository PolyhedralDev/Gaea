package org.polydev.gaea.structures;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.polydev.gaea.util.FastRandom;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * Representation of Vanilla Structure Block structure.
 *
 * @author dfsek
 * @since 2.0.0
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class NMSStructure {
    private static final String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    public static int pasteMethodIndex;
    public static int getNBTListMethodIndex;
    private static Class definedStructureClass;
    private static MethodAccess definedStructureMethodAccess;
    private static ConstructorAccess<?> definedStructureConstructorAccess;
    private static int loadStructureMethodIndex;
    private static int getStructureAsNBTMethodIndex;
    private static MethodAccess nbtStreamToolsAccess;
    private static int loadNBTStreamFromInputStreamIndex;
    private static ConstructorAccess<?> compoundNBTConstructorAccess;
    private static MethodAccess compoundNBTTagMethodAccess;
    private static MethodAccess listNBTTagMethodAccess;
    private static int getNBTListItemMethodIndex;
    private static MethodAccess enumBlockRotationMethodAccess;
    private static int enumBlockRotationValueOfIndex;
    private static MethodAccess craftWorldMethodAccess;
    private static int getCraftWorldHandleIndex;
    private static MethodAccess definedStructureInfoMethodAccess;
    private static int setRotationMethodIndex;
    private static ConstructorAccess<?> definedStructureInfoConstructorAccess;
    private static int mysteryBooleanMethodIndex;
    private static int chunkCoordIntPairMethodIndex;
    private static int mysteryBooleancMethodIndex;
    private static int setRandomMethodIndex;
    private static MethodAccess craftBlockMethodAccess;
    private static int craftBlockGetPositionIndex;

    static {
        try {
            long start = System.nanoTime();
            Bukkit.getLogger().info("[Gaea] Beginning reflections for net.minecraft.server." + version + ".");
            Class craftWorldClass = Class.forName("org.bukkit.craftbukkit." + version + ".CraftWorld");
            Class compoundNBTTagClass = Class.forName("net.minecraft.server." + version + ".NBTTagCompound");
            Class definedStructureInfoClass = Class.forName("net.minecraft.server." + version + ".DefinedStructureInfo");
            Class blockPositionClass = Class.forName("net.minecraft.server." + version + ".BlockPosition");
            Class nbtStreamToolsClass = Class.forName("net.minecraft.server." + version + ".NBTCompressedStreamTools");
            definedStructureClass = Class.forName("net.minecraft.server." + version + ".DefinedStructure");
            Class enumBlockRotationClass = Class.forName("net.minecraft.server." + version + ".EnumBlockRotation");
            Class listNBTTagClass = Class.forName("net.minecraft.server." + version + ".NBTTagList");
            Class chunkCoordIntPairClass = Class.forName("net.minecraft.server." + version + ".ChunkCoordIntPair");
            Class craftBlockClass = Class.forName("org.bukkit.craftbukkit." + version + ".block.CraftBlock");
            Class generatorAccessClass;
            if(version.startsWith("v1_15") || version.startsWith("v1_16_R1"))
                generatorAccessClass = Class.forName("net.minecraft.server." + version + ".GeneratorAccess");
            else generatorAccessClass = Class.forName("net.minecraft.server." + version + ".WorldAccess");
            nbtStreamToolsAccess = MethodAccess.get(nbtStreamToolsClass);
            loadNBTStreamFromInputStreamIndex = nbtStreamToolsAccess.getIndex("a", InputStream.class);
            definedStructureConstructorAccess = ConstructorAccess.get(definedStructureClass);
            definedStructureMethodAccess = MethodAccess.get(definedStructureClass);
            loadStructureMethodIndex = definedStructureMethodAccess.getIndex("b", compoundNBTTagClass);
            getStructureAsNBTMethodIndex = definedStructureMethodAccess.getIndex("a", compoundNBTTagClass);
            if(version.startsWith("v1_15"))
                pasteMethodIndex = definedStructureMethodAccess.getIndex("a", generatorAccessClass, blockPositionClass, definedStructureInfoClass);
            else
                pasteMethodIndex = definedStructureMethodAccess.getIndex("a", generatorAccessClass, blockPositionClass, definedStructureInfoClass, Random.class);
            compoundNBTConstructorAccess = ConstructorAccess.get(compoundNBTTagClass);
            compoundNBTTagMethodAccess = MethodAccess.get(compoundNBTTagClass);
            getNBTListMethodIndex = compoundNBTTagMethodAccess.getIndex("getList", String.class, int.class);
            listNBTTagMethodAccess = MethodAccess.get(listNBTTagClass);
            getNBTListItemMethodIndex = listNBTTagMethodAccess.getIndex("e", int.class);
            enumBlockRotationMethodAccess = MethodAccess.get(enumBlockRotationClass);
            enumBlockRotationValueOfIndex = enumBlockRotationMethodAccess.getIndex("valueOf", String.class);
            craftWorldMethodAccess = MethodAccess.get(craftWorldClass);
            getCraftWorldHandleIndex = craftWorldMethodAccess.getIndex("getHandle");
            definedStructureInfoMethodAccess = MethodAccess.get(definedStructureInfoClass);
            definedStructureInfoConstructorAccess = ConstructorAccess.get(definedStructureInfoClass);
            setRotationMethodIndex = definedStructureInfoMethodAccess.getIndex("a", enumBlockRotationClass);
            mysteryBooleanMethodIndex = definedStructureInfoMethodAccess.getIndex("a", boolean.class);
            chunkCoordIntPairMethodIndex = definedStructureInfoMethodAccess.getIndex("a", chunkCoordIntPairClass);
            mysteryBooleancMethodIndex = definedStructureInfoMethodAccess.getIndex("c", boolean.class);
            setRandomMethodIndex = definedStructureInfoMethodAccess.getIndex("a", Random.class);
            craftBlockMethodAccess = MethodAccess.get(craftBlockClass);
            craftBlockGetPositionIndex = craftBlockMethodAccess.getIndex("getPosition");
            Bukkit.getLogger().info("[Gaea] Finished reflections. Time elapsed: " + ((double) (System.nanoTime() - start)) / 1000000 + "ms");
        } catch(ClassNotFoundException e) {
            Bukkit.getLogger().severe("[Gaea] An error occurred whilst initializing Reflection. Please report this.");
            e.printStackTrace();
            Bukkit.getLogger().severe("[Gaea] Report the above error to Gaea!");
            Bukkit.getLogger().severe("[Gaea] This is most likely caused by running the plugin on an unsupported version.");
        }
    }

    private int[] dimension;
    private Object structure;
    private Location origin;
    private int rotation = 0;

    /**
     * Load a structure from an InputStream.
     *
     * @param origin - The origin location of the structure.
     * @param file   - The InputStream from which to load the structure.
     * @author dfsek
     * @since 3.5.0
     * @deprecated
     */
    @Deprecated
    public NMSStructure(Location origin, InputStream file) {
        Object structure;
        try {
            structure = definedStructureConstructorAccess.newInstance();
            definedStructureMethodAccess.invoke(structure, loadStructureMethodIndex, nbtStreamToolsAccess.invoke(null, loadNBTStreamFromInputStreamIndex, file));
            Object tag = definedStructureMethodAccess.invoke(structure, getStructureAsNBTMethodIndex, compoundNBTConstructorAccess.newInstance());
            Object dimTag = compoundNBTTagMethodAccess.invoke(tag, getNBTListMethodIndex, "size", 3);
            this.dimension = new int[] {(int) listNBTTagMethodAccess.invoke(dimTag, getNBTListItemMethodIndex, 0),
                    (int) listNBTTagMethodAccess.invoke(dimTag, getNBTListItemMethodIndex, 1),
                    (int) listNBTTagMethodAccess.invoke(dimTag, getNBTListItemMethodIndex, 2)};
            this.structure = structure;
            this.origin = origin;
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
        try {
            file.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load a structure from an Object.
     *
     * @param origin - The origin location of the structure.
     * @param file   - The InputStream from which to load the structure.
     * @author dfsek
     * @since 4.0.0
     */
    public NMSStructure(Location origin, Object file) {
        if(file.getClass() != definedStructureClass)
            throw new IllegalArgumentException("Object is not member of required class!");
        try {
            Object tag = definedStructureMethodAccess.invoke(file, getStructureAsNBTMethodIndex, compoundNBTConstructorAccess.newInstance());
            Object dimTag = compoundNBTTagMethodAccess.invoke(tag, getNBTListMethodIndex, "size", 3);
            this.dimension = new int[] {(int) listNBTTagMethodAccess.invoke(dimTag, getNBTListItemMethodIndex, 0),
                    (int) listNBTTagMethodAccess.invoke(dimTag, getNBTListItemMethodIndex, 1),
                    (int) listNBTTagMethodAccess.invoke(dimTag, getNBTListItemMethodIndex, 2)};
            this.structure = file;
            this.origin = origin;
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the NBT Tag object from an InputStream.<br>
     * Use for loading structure data into memory.
     *
     * @param file InputStream to load from.
     * @return Object - The NBT Tag object
     */
    public static Object getAsTag(InputStream file) {
        Object structure;
        try {
            structure = definedStructureConstructorAccess.newInstance();
            definedStructureMethodAccess.invoke(structure, loadStructureMethodIndex, nbtStreamToolsAccess.invoke(null, loadNBTStreamFromInputStreamIndex, file));
            return structure;
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Loads the class, use to initialize reflections before generation begins.
     *
     * @author dfsek
     * @since 3.6.5
     */
    public static void load() {
    }

    /**
     * Gets the origin of a structure.
     *
     * @return Location - The origin of the structure
     * @author dfsek
     * @since 2.0.0
     */
    public Location getOrigin() {
        return this.origin;
    }

    public void setOrigin(Location origin) {
        this.origin = origin;
    }

    /**
     * Gets the dimensions of a structure.
     *
     * @return int[] - The X, Y, and Z dimensions of the structure
     * @author dfsek
     * @since 2.0.0
     */
    public int[] getDimensions() {
        return this.dimension;
    }

    /**
     * Gets the rotation of a structure.
     *
     * @return int - The rotation of the structure
     * @author dfsek
     * @since 2.0.0
     */
    public int getRotation() {
        return this.rotation;
    }

    /**
     * Sets the rotation of structure.
     *
     * @param rotation - The rotation (in degrees)
     * @author dfsek
     * @since 2.0.0
     */
    public void setRotation(int rotation) {
        if(rotation % 90 != 0 || rotation > 360)
            throw new IllegalArgumentException("Invalid rotation provided. Rotation must be multiple of 90.");
        this.rotation = rotation;
    }

    /**
     * Gets the locations containing the structure.
     *
     * @return Location[] - The top and bottom bounding locations.
     * @author dfsek
     * @since 2.0.0
     */
    public Location[] getBoundingLocations() {
        switch(this.rotation) {
            case 0:
            case 360:
                return new Location[] {this.origin,
                        new Location(this.origin.getWorld(), this.origin.getX() + this.getX(), this.origin.getY() + this.getY(), this.origin.getZ() + this.getZ())};
            case 90:
                return new Location[] {this.origin,
                        new Location(this.origin.getWorld(), this.origin.getX() - this.getZ(), this.origin.getY() + this.getY(), this.origin.getZ() + this.getX())};
            case 180:
                return new Location[] {this.origin,
                        new Location(this.origin.getWorld(), this.origin.getX() - this.getX(), this.origin.getY() + this.getY(), this.origin.getZ() - this.getZ())};
            case 270:
                return new Location[] {this.origin,
                        new Location(this.origin.getWorld(), this.origin.getX() + this.getZ(), this.origin.getY() + this.getY(), this.origin.getZ() - this.getX())};
            default:
                throw new IllegalArgumentException("Invalid rotation provided. Rotation must be multiple of 90.");
        }
    }

    /**
     * Gets the X dimension of a structure.
     *
     * @return int - The X dimension of the structure
     * @author dfsek
     * @since 2.0.0
     */
    public int getX() {
        return this.dimension[0];
    }

    /**
     * Gets the Y dimension of a structure.
     *
     * @return int - The Y dimension of the structure
     * @author dfsek
     * @since 2.0.0
     */
    public int getY() {
        return this.dimension[1];
    }

    /**
     * Gets the Z dimension of a structure.
     *
     * @return int - The Z dimension of the structure
     * @author dfsek
     * @since 2.0.0
     */
    public int getZ() {
        return this.dimension[2];
    }

    /**
     * Pastes a structure into the world.
     *
     * @author dfsek
     * @since 2.0.0
     */
    public void paste() {
        try {
            Object rot;
            switch(this.rotation) {
                case 0:
                case 360:
                    rot = enumBlockRotationMethodAccess.invoke(null, enumBlockRotationValueOfIndex, "NONE");
                    break;
                case 90:
                    rot = enumBlockRotationMethodAccess.invoke(null, enumBlockRotationValueOfIndex, "CLOCKWISE_90");
                    break;
                case 180:
                    rot = enumBlockRotationMethodAccess.invoke(null, enumBlockRotationValueOfIndex, "CLOCKWISE_180");
                    break;
                case 270:
                    rot = enumBlockRotationMethodAccess.invoke(null, enumBlockRotationValueOfIndex, "COUNTERCLOCKWISE_90");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid rotation provided. Rotation must be multiple of 90.");
            }

            Object world = craftWorldMethodAccess.invoke(this.origin.getWorld(), getCraftWorldHandleIndex);
            Object info = definedStructureInfoMethodAccess.invoke(definedStructureInfoConstructorAccess.newInstance(), setRotationMethodIndex, rot);

            Object pos = craftBlockMethodAccess.invoke(this.origin.getBlock(), craftBlockGetPositionIndex);

            info = definedStructureInfoMethodAccess.invoke(info, mysteryBooleanMethodIndex, false);
            info = definedStructureInfoMethodAccess.invoke(info, chunkCoordIntPairMethodIndex, (Object) null);
            info = definedStructureInfoMethodAccess.invoke(info, mysteryBooleancMethodIndex, false);
            info = definedStructureInfoMethodAccess.invoke(info, setRandomMethodIndex, new FastRandom());

            if(version.startsWith("v1_15")) {
                definedStructureMethodAccess.invoke(this.structure, pasteMethodIndex, world, pos, info);
            } else {
                definedStructureMethodAccess.invoke(this.structure, pasteMethodIndex, world, pos, info, new FastRandom());
            }
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
