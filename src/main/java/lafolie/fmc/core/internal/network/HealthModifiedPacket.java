package lafolie.fmc.core.internal.network;

import java.util.HashMap;
import java.util.Map;

import lafolie.fmc.core.config.FMCConfig;
import lafolie.fmc.core.util.FMCIdentifier;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class HealthModifiedPacket
{
	public static final Identifier ID = FMCIdentifier.packetID("health_modified");

	public static enum EventType
	{
		NORMAL((byte)0),
		RESIST((byte)1),
		EFFECTIVE((byte)2),
		HEAL((byte)3),
		POISON((byte)4);

		private byte value;
		private static final Map<Byte, EventType> map = new HashMap<>();

		private EventType(byte value)
		{
			this.value = value;
		}

		static
		{
			for(EventType e : EventType.values())
			{
				map.put(e.value, e);
			}
		}

		public static EventType fromByte(byte x)
		{
			return map.get(x);
		}

		public byte getValue()
		{
			return value;
		}

	}

	public final int entityID;
	public final EventType type;
	public final float amount;

	public HealthModifiedPacket(int entityID, EventType type, float amount)
	{
		this.entityID = entityID;
		this.type = type;
		this.amount = amount;
	}

	public HealthModifiedPacket(PacketByteBuf buf)
	{
		entityID = buf.readInt();
		type = EventType.fromByte(buf.readByte());
		amount = buf.readFloat();
	}

	public PacketByteBuf write(PacketByteBuf buf)
	{	
		buf.writeInt(entityID);
		buf.writeByte(type.getValue());
		buf.writeFloat(amount);
		return buf;
	}
}