package xyz.xy718.getdrops.data.model.data;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.value.mutable.OptionalValue;

import com.google.common.reflect.TypeToken;

public interface EntityOwnerPlayerData extends DataManipulator<EntityOwnerPlayerData ,ImmutableEntityOwnerPlayerData>{
	OptionalValue<String> ownerPlayerName();
}
