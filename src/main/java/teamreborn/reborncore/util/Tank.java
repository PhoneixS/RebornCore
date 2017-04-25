package teamreborn.reborncore.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.List;

public class Tank extends FluidTank {

	public final List<Fluid> allowedFluids = new ArrayList<>();
	private final String name;
	Fluid lastFluid;
	private FluidStack lastBeforeUpdate = null;

	public Tank(String name, int capacity, TileEntity tile) {
		super(capacity);
		this.name = name;
		this.tile = tile;
		allowedFluids.addAll(FluidRegistry.getRegisteredFluids().values());
	}

	public Tank(String name, int capacity, TileEntity tile, Fluid... allowedFluids) {
		super(capacity);
		this.name = name;
		this.tile = tile;
		for (Fluid fluid : allowedFluids) {
			this.allowedFluids.add(fluid);
		}
	}

	public boolean isEmpty() {
		return getFluid() == null || getFluid().amount <= 0;
	}

	public boolean isFull() {
		return getFluid() != null && getFluid().amount >= getCapacity();
	}

	public Fluid getFluidType() {
		return getFluid() != null ? getFluid().getFluid() : null;
	}

	@Override
	public final NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound tankData = new NBTTagCompound();
		super.writeToNBT(tankData);
		nbt.setTag(name, tankData);
		return nbt;
	}

	public void setFluidAmount(int amount) {
		if (fluid != null) {
			fluid.amount = amount;
		}
	}

	@Override
	public final FluidTank readFromNBT(NBTTagCompound nbt) {
		if (nbt.hasKey(name)) {
			// allow to read empty tanks
			setFluid(null);

			NBTTagCompound tankData = nbt.getCompoundTag(name);
			super.readFromNBT(tankData);
		}
		return this;
	}

	@Override
	public boolean canFillFluidType(FluidStack fluid) {
		for (Fluid allowedFluid : allowedFluids)
			if (fluid.getFluid().equals(allowedFluid)) {
				return true;
			}
		return false;
	}

	//	//TODO optimise & change to new packet system
	//	public void compareAndUpdate() {
	//		if (tile == null || tile.getWorld().isRemote) {
	//			return;
	//		}
	//		if (lastFluid == null || (lastFluid != null && (this.getFluid() == null) || this.getFluid().getFluid() == null) || (lastFluid != this.getFluid().getFluid())) {
	//			if (this.getFluid() == null) {
	//				lastFluid = null;
	//			} else {
	//				lastFluid = this.getFluid().getFluid();
	//			}
	//			reborncore.common.network.NetworkManager.sendToAllAround(new CustomDescriptionPacket(tile), new NetworkRegistry.TargetPoint(tile.getWorld().provider.getDimension(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), 20));
	//		}
	//	}

}