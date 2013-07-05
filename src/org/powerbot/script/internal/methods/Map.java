package org.powerbot.script.internal.methods;

import org.powerbot.client.Client;
import org.powerbot.client.RSAnimableNode;
import org.powerbot.client.RSGround;
import org.powerbot.client.RSGroundBytes;
import org.powerbot.client.RSGroundInfo;
import org.powerbot.client.RSInfo;
import org.powerbot.client.RSObject;
import org.powerbot.client.RSRotatableObject;
import org.powerbot.script.internal.wrappers.CollisionMap;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.methods.MethodProvider;
import org.powerbot.script.wrappers.GameObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Map extends MethodProvider {
	public Map(MethodContext factory) {
		super(factory);
	}

	private final GameObject.Type[] TYPES = {
			GameObject.Type.BOUNDARY, GameObject.Type.BOUNDARY,
			GameObject.Type.FLOOR_DECORATION,
			GameObject.Type.WALL_DECORATION, GameObject.Type.WALL_DECORATION
	};

	public CollisionMap[] getCollisionMaps() {
		Client client = ctx.getClient();
		if (client == null) {
			return new CollisionMap[0];
		}
		final RSInfo info;
		final RSGroundInfo groundInfo;
		final RSGround[][][] grounds;
		if ((info = client.getRSGroundInfo()) == null || (groundInfo = info.getRSGroundInfo()) == null ||
				(grounds = groundInfo.getRSGroundArray()) == null) {
			return new CollisionMap[0];
		}
		RSGroundBytes ground = info.getGroundBytes();
		byte[][][] settings = ground != null ? ground.getBytes() : null;
		if (settings == null) return new CollisionMap[0];
		CollisionMap[] collisionMaps = new CollisionMap[settings.length];
		for (int plane = 0; plane < collisionMaps.length; plane++) {
			int xSize = settings[plane].length;
			int ySize = Integer.MAX_VALUE;
			for (int x = 0; x < xSize; x++) {
				ySize = Math.min(ySize, settings[plane][x].length);
			}
			collisionMaps[plane] = new CollisionMap(xSize, ySize);
			for (int locX = 0; locX < xSize; locX++) {
				for (int locY = 0; locY < ySize; locY++) {
					List<GameObject> objects = getObjects(locX, locY, plane, grounds);
					if ((settings[plane][locX][locY] & 0x1) == 0) {
						readCollision(collisionMaps[plane], locX, locY, objects);
						continue;
					}
					readCollision(collisionMaps[plane], locX, locY, objects);
					int planeOffset = plane;
					if ((settings[1][locX][locY] & 0x2) != 0) {
						planeOffset--;
					}
					if (planeOffset < 0) {
						continue;
					}
					if (collisionMaps[planeOffset] == null) {
						collisionMaps[planeOffset] = new CollisionMap(xSize, ySize);
					}
					collisionMaps[planeOffset].markDeadBlock(locX, locY);
				}
			}
		}
		return collisionMaps;
	}

	private List<GameObject> getObjects(int x, int y, int plane, RSGround[][][] grounds) {
		List<GameObject> items = new ArrayList<>();
		RSGround ground;
		if (plane < grounds.length && x < grounds[plane].length && y < grounds[plane][x].length) {
			ground = grounds[plane][x][y];
		} else {
			return items;
		}
		if (ground == null) return items;

		for (RSAnimableNode animable = ground.getRSAnimableList(); animable != null; animable = animable.getNext()) {
			Object node = animable.getRSAnimable();
			if (node == null || !(node instanceof RSObject)) continue;
			RSObject obj = (RSObject) node;
			if (obj.getId() != -1) {
				items.add(new GameObject(ctx, obj, GameObject.Type.INTERACTIVE));
			}
		}

		final RSObject[] objs = {
				ground.getBoundary1(), ground.getBoundary2(),
				ground.getFloorDecoration(),
				ground.getWallDecoration1(), ground.getWallDecoration2()
		};

		for (int i = 0; i < objs.length; i++) {
			if (objs[i] != null && objs[i].getId() != -1) {
				items.add(new GameObject(ctx, objs[i], TYPES[i]));
			}
		}
		return items;
	}

	private void readCollision(final CollisionMap collisionMap, final int localX, final int localY, final List<GameObject> objects) {
		int clippingType;
		for (GameObject next : objects) {
			clippingType = GameObject.clippingTypeForId(next.getId());
			switch (next.getType()) {
			case BOUNDARY:
				if (clippingType == 0) continue;

				RSObject object = rsObject(next);
				if (object == null) continue;
				RSRotatableObject rot = (RSRotatableObject) object;
				collisionMap.markWall(localX, localY, rot.getType(), rot.getOrientation(), false);
				break;
			case FLOOR_DECORATION:
				if (clippingType != 1) continue;
				collisionMap.markDecoration(localX, localY);

				break;
			case INTERACTIVE:
				if (clippingType == 0) continue;
				collisionMap.markInteractive(localX, localY, false);
				break;
			}
		}
	}

	private RSObject rsObject(GameObject object) {
		try {
			Field field = object.getClass().getDeclaredField("object");
			boolean accessable = field.isAccessible();
			field.setAccessible(true);
			WeakReference<RSObject> reference = (WeakReference<RSObject>) field.get(object);
			if (reference != null) {
				return reference.get();
			}
			field.setAccessible(accessable);
		} catch (NoSuchFieldException | IllegalAccessException ignored) {
		}
		return null;
	}

	private CollisionMap getCollisionMap(int plane) {
		CollisionMap[] maps = getCollisionMaps();
		if (plane < 0 || plane >= maps.length) {
			return null;
		}
		return maps[plane];
	}
}
