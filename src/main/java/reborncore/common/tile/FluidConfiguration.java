package reborncore.common.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FluidConfiguration implements INBTSerializable<NBTTagCompound> {

	HashMap<EnumFacing, FluidConfig> sideMap;
	boolean input, output;

	public FluidConfiguration() {
		sideMap = new HashMap<>();
		Arrays.stream(EnumFacing.VALUES).forEach(facing -> sideMap.put(facing, new FluidConfig(facing)));
	}

	public FluidConfiguration(NBTTagCompound tagCompound) {
		sideMap = new HashMap<>();
		deserializeNBT(tagCompound);
	}

	public FluidConfig getSideDetail(EnumFacing side) {
		if(side == null){
			return sideMap.get(EnumFacing.NORTH);
		}
		return sideMap.get(side);
	}

	public List<FluidConfig> getAllSides() {
		return new ArrayList<>(sideMap.values());
	}

	public void updateFluidConfig(FluidConfig config) {
		FluidConfig toEdit = sideMap.get(config.side);
		toEdit.ioConfig = config.ioConfig;
	}

	public void update(TileLegacyMachineBase machineBase) {
		if (!input && !output) {
			return;
		}
		//TODO handle push and pull of fluids
	}

	public boolean autoInput() {
		return input;
	}

	public boolean autoOutput() {
		return output;
	}

	public void setInput(boolean input) {
		this.input = input;
	}

	public void setOutput(boolean output) {
		this.output = output;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		Arrays.stream(EnumFacing.VALUES).forEach(facing -> compound.setTag("side_" + facing.ordinal(), sideMap.get(facing).serializeNBT()));
		compound.setBoolean("input", input);
		compound.setBoolean("output", output);
		return compound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		sideMap.clear();
		Arrays.stream(EnumFacing.VALUES).forEach(facing -> {
			NBTTagCompound compound = nbt.getCompoundTag("side_" + facing.ordinal());
			FluidConfig config = new FluidConfig(compound);
			sideMap.put(facing, config);
		});
		input = nbt.getBoolean("input");
		output = nbt.getBoolean("output");
	}

	public static class FluidConfig implements INBTSerializable<NBTTagCompound> {
		EnumFacing side;
		FluidConfiguration.ExtractConfig ioConfig;

		public FluidConfig(EnumFacing side) {
			this.side = side;
			this.ioConfig = ExtractConfig.ALL;
		}

		public FluidConfig(EnumFacing side, FluidConfiguration.ExtractConfig ioConfig) {
			this.side = side;
			this.ioConfig = ioConfig;
		}

		public FluidConfig(NBTTagCompound tagCompound) {
			deserializeNBT(tagCompound);
		}

		public EnumFacing getSide() {
			return side;
		}

		public ExtractConfig getIoConfig() {
			return ioConfig;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			NBTTagCompound tagCompound = new NBTTagCompound();
			tagCompound.setInteger("side", side.ordinal());
			tagCompound.setInteger("config", ioConfig.ordinal());
			return tagCompound;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			side = EnumFacing.VALUES[nbt.getInteger("side")];
			ioConfig = FluidConfiguration.ExtractConfig.values()[nbt.getInteger("config")];
		}
	}


	public enum ExtractConfig {
		NONE(false, false),
		INPUT(false, true),
		OUTPUT(true, false),
		ALL(true, true);

		boolean extact;
		boolean insert;

		ExtractConfig(boolean extact, boolean insert) {
			this.extact = extact;
			this.insert = insert;
		}

		public boolean isExtact() {
			return extact;
		}

		public boolean isInsert() {
			return insert;
		}

		public boolean isEnabled(){
			return extact || insert;
		}

		public FluidConfiguration.ExtractConfig getNext() {
			int i = this.ordinal() + 1;
			if (i >= FluidConfiguration.ExtractConfig.values().length) {
				i = 0;
			}
			return FluidConfiguration.ExtractConfig.values()[i];
		}
	}
}
