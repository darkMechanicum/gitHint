package com.tsarev.githint.statistics.common;

import com.tsarev.githint.statistics.api.FileStatisticsProvider;
import com.tsarev.githint.statistics.api.OverallStat;
import com.tsarev.githint.statistics.api.StatEntry;
import com.tsarev.githint.vcs.api.ChangedFileContent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Statistics providers implementation based on inner statistics collectors.
 *
 * @see StatAccumulator
 */
public class CommonCompositeStatProvider implements FileStatisticsProvider<CommonStatTypes> {

    /**
     * Registered statistics accumulators.
     */
    private Map<CommonStatTypes, Supplier<StatAccumulator<CommonStatTypes, ?>>> statConstructors = new HashMap<>();

    // ---- [CommonCompositeStatProvider] Interface methods ------------------------------------------

    /** {@inheritDoc} */
    @Override
    public <DataT> StatEntry<CommonStatTypes, DataT> getStatFor(Class<DataT> dataClass,
                                                                CommonStatTypes statType,
                                                                ChangedFileContent changed) {
        StatAccumulator<CommonStatTypes, DataT> accumulator = constructAccumulator(dataClass, statType);
        accumulator.addData(changed);
        return accumulator.getStat();
    }

    /** {@inheritDoc} */
    @Override
    public <DataT> StatEntry<CommonStatTypes, DataT> getStatFor(Class<DataT> dataClass,
                                                                CommonStatTypes statType,
                                                                List<ChangedFileContent> changed) {
        StatAccumulator<CommonStatTypes, DataT> accumulator = constructAccumulator(dataClass, statType);
        addAllToAccumulator(accumulator, changed);
        return accumulator.getStat();
    }

    /** {@inheritDoc} */
    @Override
    public <DataT> StatEntry<CommonStatTypes, DataT> getStatFor(Class<DataT> dataClass,
                                                                CommonStatTypes statType,
                                                                Stream<ChangedFileContent> changed) {
        StatAccumulator<CommonStatTypes, DataT> accumulator = constructAccumulator(dataClass, statType);
        // TODO [790658] [08.05.2018] [Aleksandr.Tsarev] Change accumulator to fit Stream API.
        changed.reduce((o, current) -> {accumulator.addData(current); return o;});
        return accumulator.getStat();
    }

    /** {@inheritDoc} */
    @Override
    public OverallStat<CommonStatTypes> getAllStatFor(List<ChangedFileContent> changed) {
        return getAllStatFor(changed.stream());
    }

    /** {@inheritDoc} */
    @Override
    public OverallStat<CommonStatTypes> getAllStatFor(Stream<ChangedFileContent> changed) {
        Set<CommonStatTypes> chosenKeys = statConstructors.keySet();
        List<? extends StatAccumulator<CommonStatTypes, ?>> accumulators = chosenKeys.stream()
                .map(key -> this.constructAccumulator(key.getDataType(), key))
                .collect(Collectors.toList());

        // TODO [442657] [08.05.2018] [Aleksandr.Tsarev] Change accumulator to fit Stream API.
        changed.reduce((o, current) -> {
            accumulators.forEach(acc -> acc.addData(current));
            return o;
        });

        Map<CommonStatTypes, StatEntry<CommonStatTypes, ?>> accumulated = new HashMap<>();
        for (StatAccumulator<CommonStatTypes, ?> accumulator : accumulators) {
            accumulated.put(accumulator.getKey(), accumulator.getStat());
        }
        return new OverallStat<>(accumulated);
    }

    // ---- [CommonCompositeStatProvider] Private methods ------------------------------------------

    /**
     * Add all contents to accumulator.
     */
    private <DataT> void addAllToAccumulator(StatAccumulator<CommonStatTypes, DataT> accumulator,
                                             List<ChangedFileContent> changed) {
        for (ChangedFileContent content : changed) {
            accumulator.addData(content);
        }
    }

    /**
     * Instantiate stat accumulator.
     */
    private <DataT> StatAccumulator<CommonStatTypes, DataT> constructAccumulator(Class<DataT> dataClass,
                                                                                 CommonStatTypes statType) {
        Supplier<StatAccumulator<CommonStatTypes, ?>> constructor = statConstructors.get(statType);
        if (constructor == null) {
            // TODO change exception
            throw new RuntimeException("No constructor");
        }
        if (statType.getDataType() != dataClass) {
            // TODO change exception
            throw new RuntimeException("Wrong data class");
        }
        // We are confident here about data class, since previous check.
        return (StatAccumulator<CommonStatTypes, DataT>) constructor.get();
    }

    /**
     * Регистрация нового агрегатора.
     *
     * @return {@code true}, если новый агрегатор успешно зарегистрирован
     */
    public boolean register(CommonStatTypes aggregatorType,
                            Supplier<StatAccumulator<CommonStatTypes, ?>> aggregatorConstructor) {
        return statConstructors.putIfAbsent(aggregatorType, aggregatorConstructor) == null;
    }
}
