package io.github.bloepiloepi.basicredstone.mixins;

import io.github.bloepiloepi.basicredstone.redstone.Redstone;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceManager;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InstanceManager.class)
public class InstanceManagerMixin {
	
	@Inject(method = "unregisterInstance", at = @At("TAIL"))
	private void onUnregister(@NotNull Instance instance, CallbackInfo ci) {
		Redstone.deletePowerNet(instance);
	}
}
