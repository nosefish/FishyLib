package net.gmx.nosefish.fishylib.blocks;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.canarymod.api.world.blocks.BlockType;

public enum RedstonePowerSource {
	DetectorRail(BlockType.DetectorRail.getId()),
	DaylightSensor(BlockType.DaylightSensor.getId()),
	HeavyWeightedPressurePlate(BlockType.HeavyWeightedPressurePlate.getId()),
	Lever(BlockType.Lever.getId()),
	LightWeightedPressurePlate(BlockType.LightWeightedPressurePlate.getId()),
	RedstoneBlock(BlockType.RedstoneBlock.getId()),
	RedstoneComparatorOff(BlockType.RedstoneComparatorOff.getId()),
	RedstoneComparatorOn(BlockType.RedstoneComparatorOn.getId()),
	RedstoneRepeaterOff(BlockType.RedstoneRepeaterOff.getId()),
	RedstoneRepeaterOn(BlockType.RedstoneRepeaterOn.getId()),
	RedstoneTorchOff(BlockType.RedstoneTorchOff.getId()),
	RedstoneTorchOn(BlockType.RedstoneTorchOn.getId()),
	RedstoneWire(BlockType.RedstoneWire.getId()),
	StoneButton(BlockType.StoneButton.getId()),
	StonePlate(BlockType.StonePlate.getId()),
	TrappedChest(BlockType.TrappedChest.getId()),
	TripwireHook(BlockType.TripwireHook.getId()),
	WoodenButton(BlockType.WoodenButton.getId()),
	WoodPlate(BlockType.WoodPlate.getId());
	private int id;
	
	private static final Map<Integer, RedstonePowerSource> reverseMap = new TreeMap<Integer, RedstonePowerSource>();
	static {
		for (RedstonePowerSource value : RedstonePowerSource.values()) {
			reverseMap.put(value.getId(), value);
		}
	}
	
	public static Set<RedstonePowerSource> directionalRSBlocks =
			Collections.unmodifiableSet(EnumSet.of(
			RedstoneComparatorOff,
			RedstoneComparatorOn,
			RedstoneRepeaterOff,
			RedstoneRepeaterOn
			));
	
	public static Set<RedstonePowerSource> powerIsDataValue =
			Collections.unmodifiableSet(EnumSet.of(
			DaylightSensor,
			HeavyWeightedPressurePlate,
			LightWeightedPressurePlate,
			RedstoneWire
			));
	
	public static Set<RedstonePowerSource> powerIsAlwaysOn =
			Collections.unmodifiableSet(EnumSet.of(
			RedstoneBlock,
			RedstoneComparatorOn,
			RedstoneRepeaterOn,
			RedstoneTorchOn
			));
	
	public static Set<RedstonePowerSource> powerIsAlwaysOff =
			Collections.unmodifiableSet(EnumSet.of(
			RedstoneComparatorOff,
			RedstoneRepeaterOff,
			RedstoneTorchOff
			));
	
	public static Set<RedstonePowerSource> powerOnOffInBit0x1 =
			Collections.unmodifiableSet(EnumSet.of(
			StonePlate,
			WoodPlate
			));
	
	public static Set<RedstonePowerSource> powerOnOffInBit0x8 =
			Collections.unmodifiableSet(EnumSet.of(
			DetectorRail, // TODO: test, not documented on minecraftwiki
			Lever,
			StoneButton,
			TrappedChest, // TODO: test, not documented on minecraftwiki
			TripwireHook,
			WoodenButton
			));
	
	private RedstonePowerSource(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public static RedstonePowerSource fromId(int id) {
		return reverseMap.get(id);
	}
}
