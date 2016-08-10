/*******************************************************************************
 * Copyright (c) 2012 Mrbrutal. All rights reserved.
 * 
 * @name TrainCraft
 * @author Mrbrutal
 ******************************************************************************/

package train.common.core.handlers;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import train.common.Traincraft;
import train.common.core.network.*;
import train.common.entity.rollingStock.EntityTracksBuilder;
import train.common.Packet250CustomPayload;
import train.common.entity.zeppelin.AbstractZeppelin;
import train.common.library.Info;

public class PacketHandler {

	protected RollingStockStatsEventHandler statsEventHandler = new RollingStockStatsEventHandler();

	public static void init(){
		Traincraft.tcLog.info("Initialize Packets");
		Traincraft.modChannel = NetworkRegistry.INSTANCE.newSimpleChannel(Info.channel);
		Traincraft.keyChannel = NetworkRegistry.INSTANCE.newSimpleChannel(Info.keyChannel);
		Traincraft.rotationChannel = NetworkRegistry.INSTANCE.newSimpleChannel(Info.rotationChannel);


		Traincraft.slotschannel = NetworkRegistry.INSTANCE.newSimpleChannel("slots");
		Traincraft.ignitionChannel = NetworkRegistry.INSTANCE.newSimpleChannel("ignition");
		Traincraft.brakeChannel = NetworkRegistry.INSTANCE.newSimpleChannel("brake");
		Traincraft.lockChannel = NetworkRegistry.INSTANCE.newSimpleChannel("lock");
		Traincraft.builderChannel = NetworkRegistry.INSTANCE.newSimpleChannel("builder");



		Traincraft.keyChannel.registerMessage(PacketKeyPress.Handler.class, PacketKeyPress.class, 1, Side.SERVER);
		Traincraft.rotationChannel.registerMessage(PacketRollingStockRotation.Handler.class, PacketRollingStockRotation.class, 1, Side.CLIENT);
		//Traincraft.modChannel.registerMessage(PacketSetJukeboxStreamingUrl.Handler.class, PacketSetJukeboxStreamingUrl.class, 1, Side.SERVER);
		Traincraft.slotschannel.registerMessage(PacketSlotsFilled.Handler.class, PacketSlotsFilled.class, 1, Side.CLIENT);
		Traincraft.brakeChannel.registerMessage(PacketParkingBreak.Handler.class, PacketParkingBreak.class, 1, Side.SERVER);
		Traincraft.lockChannel.registerMessage(PacketSetTrainLockedToClient.Handler.class, PacketSetTrainLockedToClient.class, 1, Side.SERVER);
		Traincraft.ignitionChannel.registerMessage(PacketSetLocoTurnedOn.Handler.class, PacketSetLocoTurnedOn.class, 1, Side.SERVER);
		Traincraft.modChannel.registerMessage(PacketLantern.Handler.class, PacketLantern.class, 1, Side.SERVER);
		Traincraft.builderChannel.registerMessage(PacketTrackBuilderHeight.Handler.class, PacketTrackBuilderHeight.class, 1, Side.CLIENT);
	}

	public static Packet setBookPage(Entity player, int page, int recipe) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeInt(6);
			dos.writeInt(player.getEntityId());//.getID());
			dos.writeInt(page);
			dos.writeInt(recipe);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload(Info.channel, bos.toByteArray());
		packet.length = bos.size();
		if (player instanceof EntityClientPlayerMP) {
			EntityClientPlayerMP playerMP = (EntityClientPlayerMP) player;
			playerMP.sendQueue.addToSendQueue(packet);
		}
		return packet;
	}

}
