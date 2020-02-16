package xyz.xy718.getdrops.data.model.data;

import java.util.UUID;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.immutable.ImmutableOptionalValue;

public interface ImmutableEntityOwnerPlayerData extends ImmutableDataManipulator<ImmutableEntityOwnerPlayerData, EntityOwnerPlayerData>{

    ImmutableOptionalValue<String> ownerPlayerName();
}
