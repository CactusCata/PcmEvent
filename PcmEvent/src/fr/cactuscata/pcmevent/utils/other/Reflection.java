package fr.cactuscata.pcmevent.utils.other;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.Packet;

/**
 * <p>
 * Cette classe permet d'utiliser de manière simple la réfléction.
 * </p>
 * 
 * @author CactusCata
 * @version 2.5.1
 * @since 2.4.0
 */

public final class Reflection {

	public static final void setValue(final Object obj, final String name, final Object value) {

		try {
			final Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (final NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	public static final Object getValue(final Object obj, final String name) {
		try {
			final Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return field.get(obj);
		} catch (final NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static final void sendPacket(final Packet<?> packet, final Player player) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	public static final void sendPacket(final Packet<?> packet) {
		Bukkit.getOnlinePlayers().forEach(player -> sendPacket(packet, player));
	}

	public static final Constructor<?> getConstructor(final Class<?> clazz, final Class<?>... parameterTypes)
			throws NoSuchMethodException {
		final Class<?>[] primitiveTypes = DataType.getPrimitive(parameterTypes);
		for (Constructor<?> constructor : clazz.getConstructors()) {
			if (!DataType.compare(DataType.getPrimitive(constructor.getParameterTypes()), primitiveTypes)) {
				continue;
			}
			return constructor;
		}
		throw new NoSuchMethodException(
				"There is no such constructor in this class with the specified parameter types");
	}

	public static final Constructor<?> getConstructor(final String className, final PackageType packageType,
			final Class<?>... parameterTypes) throws NoSuchMethodException, ClassNotFoundException {
		return getConstructor(packageType.getClass(className), parameterTypes);
	}

	public static final Object instantiateObject(final Class<?> clazz, final Object... arguments)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException {
		return getConstructor(clazz, DataType.getPrimitive(arguments)).newInstance(arguments);
	}

	public static final Object instantiateObject(final String className, final PackageType packageType,
			final Object... arguments) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
		return instantiateObject(packageType.getClass(className), arguments);
	}

	public static final Method getMethod(final Class<?> clazz, final String methodName,
			final Class<?>... parameterTypes) throws NoSuchMethodException {
		final Class<?>[] primitiveTypes = DataType.getPrimitive(parameterTypes);
		for (final Method method : clazz.getMethods()) {
			if (!method.getName().equals(methodName)
					|| !DataType.compare(DataType.getPrimitive(method.getParameterTypes()), primitiveTypes)) {
				continue;
			}
			return method;
		}
		throw new NoSuchMethodException(
				"There is no such method in this class with the specified name and parameter types");
	}

	public static final Method getMethod(final String className, final PackageType packageType, final String methodName,
			final Class<?>... parameterTypes) throws NoSuchMethodException, ClassNotFoundException {
		return getMethod(packageType.getClass(className), methodName, parameterTypes);
	}

	public static final Object invokeMethod(final Object instance, final String methodName, final Object... arguments)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		return getMethod(instance.getClass(), methodName, DataType.getPrimitive(arguments)).invoke(instance, arguments);
	}

	public static final Object invokeMethod(final Object instance, final Class<?> clazz, final String methodName,
			final Object... arguments)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		return getMethod(clazz, methodName, DataType.getPrimitive(arguments)).invoke(instance, arguments);
	}

	public static final Object invokeMethod(final Object instance, final String className,
			final PackageType packageType, final String methodName, Object... arguments) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
		return invokeMethod(instance, packageType.getClass(className), methodName, arguments);
	}

	public static final Field getField(final Class<?> clazz, final boolean declared, final String fieldName)
			throws NoSuchFieldException, SecurityException {
		final Field field = declared ? clazz.getDeclaredField(fieldName) : clazz.getField(fieldName);
		field.setAccessible(true);
		return field;
	}

	public static final Field getField(final String className, final PackageType packageType, final boolean declared, final String fieldName)
			throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		return getField(packageType.getClass(className), declared, fieldName);
	}

	public static final Object getValue(final Object instance, final Class<?> clazz, final boolean declared, final String fieldName)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		return getField(clazz, declared, fieldName).get(instance);
	}

	public static final Object getValue(final Object instance, final String className, final PackageType packageType, final boolean declared,
			final String fieldName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException,
			SecurityException, ClassNotFoundException {
		return getValue(instance, packageType.getClass(className), declared, fieldName);
	}

	public static final Object getValue(final Object instance, final boolean declared, final String fieldName)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		return getValue(instance, instance.getClass(), declared, fieldName);
	}

	public static final void setValue(final Object instance, final Class<?> clazz, final boolean declared, final String fieldName, final Object value)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		getField(clazz, declared, fieldName).set(instance, value);
	}

	public static final void setValue(final Object instance, final String className, final PackageType packageType, final boolean declared,
			final String fieldName, final Object value) throws IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException, ClassNotFoundException {
		setValue(instance, packageType.getClass(className), declared, fieldName, value);
	}

	public static final void setValue(final Object instance, final boolean declared, final String fieldName, final Object value)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		setValue(instance, instance.getClass(), declared, fieldName, value);
	}

	public static final void putInPrivateStaticMap(final Class<?> clazz, final String fieldName, final Object key, final Object value)
			throws Exception {
		final Field field = clazz.getDeclaredField(fieldName);
		field.setAccessible(true);
		@SuppressWarnings("unchecked")
		Map<Object, Object> map = (Map<Object, Object>) field.get(null);
		map.put(key, value);
	}

	public enum PackageType {
		MINECRAFT_SERVER("net.minecraft.server." + getServerVersion()),
		CRAFTBUKKIT("org.bukkit.craftbukkit." + getServerVersion()),
		CRAFTBUKKIT_BLOCK(CRAFTBUKKIT, "block"),
		CRAFTBUKKIT_CHUNKIO(CRAFTBUKKIT, "chunkio"),
		CRAFTBUKKIT_COMMAND(CRAFTBUKKIT, "command"),
		CRAFTBUKKIT_CONVERSATIONS(CRAFTBUKKIT, "conversations"),
		CRAFTBUKKIT_ENCHANTMENS(CRAFTBUKKIT, "enchantments"),
		CRAFTBUKKIT_ENTITY(CRAFTBUKKIT, "entity"),
		CRAFTBUKKIT_EVENT(CRAFTBUKKIT, "event"),
		CRAFTBUKKIT_GENERATOR(CRAFTBUKKIT, "generator"),
		CRAFTBUKKIT_HELP(CRAFTBUKKIT, "help"),
		CRAFTBUKKIT_INVENTORY(CRAFTBUKKIT, "inventory"),
		CRAFTBUKKIT_MAP(CRAFTBUKKIT, "map"),
		CRAFTBUKKIT_METADATA(CRAFTBUKKIT, "metadata"),
		CRAFTBUKKIT_POTION(CRAFTBUKKIT, "potion"),
		CRAFTBUKKIT_PROJECTILES(CRAFTBUKKIT, "projectiles"),
		CRAFTBUKKIT_SCHEDULER(CRAFTBUKKIT, "scheduler"),
		CRAFTBUKKIT_SCOREBOARD(CRAFTBUKKIT, "scoreboard"),
		CRAFTBUKKIT_UPDATER(CRAFTBUKKIT, "updater"),
		CRAFTBUKKIT_UTIL(CRAFTBUKKIT, "util");

		private final String path;

		private PackageType(final String path) {
			this.path = path;
		}

		private PackageType(final PackageType parent, final String path) {
			this(parent + "." + path);
		}

		public final String getPath() {
			return path;
		}

		public final Class<?> getClass(final String className) throws ClassNotFoundException {
			return Class.forName(this + "." + className);
		}

		@Override
		public final String toString() {
			return this.path;
		}

		public static final String getServerVersion() {
			return Bukkit.getServer().getClass().getPackage().getName().substring(23);
		}
	}

	public enum DataType {
		BYTE(byte.class, Byte.class),
		SHORT(short.class, Short.class),
		INTEGER(int.class, Integer.class),
		LONG(long.class, Long.class),
		CHARACTER(char.class, Character.class),
		FLOAT(float.class, Float.class),
		DOUBLE(double.class, Double.class),
		BOOLEAN(boolean.class, Boolean.class);

		private static final Map<Class<?>, DataType> CLASS_MAP = new HashMap<Class<?>, DataType>();
		private final Class<?> primitive;
		private final Class<?> reference;

		static {
			for (final DataType type : values()) {
				CLASS_MAP.put(type.primitive, type);
				CLASS_MAP.put(type.reference, type);
			}
		}

		private DataType(final Class<?> primitive, final Class<?> reference) {
			this.primitive = primitive;
			this.reference = reference;
		}

		public final Class<?> getPrimitive() {
			return primitive;
		}

		public final Class<?> getReference() {
			return reference;
		}

		public static final DataType fromClass(final Class<?> clazz) {
			return CLASS_MAP.get(clazz);
		}

		public static final Class<?> getPrimitive(final Class<?> clazz) {
			final DataType type = fromClass(clazz);
			return type == null ? clazz : type.getPrimitive();
		}

		public static final Class<?> getReference(final Class<?> clazz) {
			final DataType type = fromClass(clazz);
			return type == null ? clazz : type.getReference();
		}

		public static final Class<?>[] getPrimitive(final Class<?>[] classes) {
			final int length = classes == null ? 0 : classes.length;
			final Class<?>[] types = new Class<?>[length];
			for (int index = 0; index < length; index++) {
				types[index] = getPrimitive(classes[index]);
			}
			return types;
		}

		public static final Class<?>[] getReference(final Class<?>[] classes) {
			final int length = classes == null ? 0 : classes.length;
			final Class<?>[] types = new Class<?>[length];
			for (int index = 0; index < length; index++) {
				types[index] = getReference(classes[index]);
			}
			return types;
		}

		public static final Class<?>[] getPrimitive(final Object[] objects) {
			final int length = objects == null ? 0 : objects.length;
			final Class<?>[] types = new Class<?>[length];
			for (int index = 0; index < length; index++) {
				types[index] = getPrimitive(objects[index].getClass());
			}
			return types;
		}

		public static final Class<?>[] getReference(final Object[] objects) {
			final int length = objects == null ? 0 : objects.length;
			final Class<?>[] types = new Class<?>[length];
			for (int index = 0; index < length; index++) {
				types[index] = getReference(objects[index].getClass());
			}
			return types;
		}

		public static final boolean compare(final Class<?>[] primary, final Class<?>[] secondary) {
			if (primary == null || secondary == null || primary.length != secondary.length) {
				return false;
			}
			for (int index = 0; index < primary.length; index++) {
				final Class<?> primaryClass = primary[index];
				final Class<?> secondaryClass = secondary[index];
				if (primaryClass.equals(secondaryClass) || primaryClass.isAssignableFrom(secondaryClass)) {
					continue;
				}
				return false;
			}
			return true;
		}
	}

}
