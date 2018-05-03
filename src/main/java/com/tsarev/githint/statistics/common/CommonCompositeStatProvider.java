package com.tsarev.githint.statistics.common;

import com.tsarev.githint.statistics.api.FileStatisticsProvider;
import com.tsarev.githint.statistics.api.OverallStat;
import com.tsarev.githint.statistics.api.StatEntry;
import com.tsarev.githint.vcs.api.ChangedFileContent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Простая реализация получения статистики.
 */
public class CommonCompositeStatProvider implements FileStatisticsProvider<CommonStatTypes> {

    /**
     * Зарегистрированные агрегаторы статистики.
     */
    private Map<CommonStatTypes, Supplier<StatAccumulator<CommonStatTypes, ?>>> statConstructors = new HashMap<>();

    @Override
    public <DataT> StatEntry<CommonStatTypes, DataT> getStatFor(Class<DataT> dataClass,
                                                                CommonStatTypes statType,
                                                                ChangedFileContent changed) {
        StatAccumulator<CommonStatTypes, DataT> accumulator = constructAccumulator(dataClass, statType);
        accumulator.addData(changed);
        return accumulator.getStat();
    }

    @Override
    public <DataT> StatEntry<CommonStatTypes, DataT> getStatFor(Class<DataT> dataClass,
                                                                CommonStatTypes statType,
                                                                List<ChangedFileContent> changed) {
        StatAccumulator<CommonStatTypes, DataT> accumulator = constructAccumulator(dataClass, statType);
        addAllToAccumulator(accumulator, changed);
        return accumulator.getStat();
    }

    @Override
    public OverallStat<CommonStatTypes> getAllStatFor(List<ChangedFileContent> changed) {
        Map<CommonStatTypes, StatEntry<CommonStatTypes, ?>> accumulated = new HashMap<>();
        for (CommonStatTypes accumulatorType : statConstructors.keySet()) {
            StatAccumulator<CommonStatTypes, ?> accumulator = constructAccumulator(accumulatorType.getDataType(), accumulatorType);
            addAllToAccumulator(accumulator, changed);
            accumulated.put(accumulatorType, accumulator.getStat());
        }
        return new OverallStat<>(accumulated);
    }

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
