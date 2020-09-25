package com.bosch.rcm.service;

import com.bosch.rcm.domain.CacheData;
import com.bosch.rcm.domain.RcmLatestErrorData;
import com.bosch.rcm.domain.Signal;
import com.bosch.rcm.domain.SignalError;
import com.bosch.rcm.domain.constant.SignalConstants;
import com.bosch.rcm.domain.dto.AggregationDTO.SignalErrorDTO;
import com.bosch.rcm.repository.CacheDataRepository;
import com.bosch.rcm.repository.SignalRepository;
import com.bosch.rcm.service.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class SignalErrorService {
    @Resource(name = "latestErrorData")
    RcmLatestErrorData latestErrorData;
    private final Sort timestampSort = new Sort(Sort.Direction.ASC, "timestamp");
    private final SignalRepository signalRepository;
    private final CacheDataRepository cacheDataRepository;
    private final MongoTemplate mongoTemplate;
    private final DateUtil dateUtil;
    private final Logger logger = LoggerFactory.getLogger(SignalErrorService.class);

    public SignalErrorService(SignalRepository signalRepository, CacheDataRepository cacheDataRepository,
                              MongoTemplate mongoTemplate, DateUtil dateUtil) {
        this.signalRepository = signalRepository;
        this.cacheDataRepository = cacheDataRepository;
        this.mongoTemplate = mongoTemplate;
        this.dateUtil = dateUtil;
    }

    @PostConstruct
    @Async
    public void setApplicationData() {
        latestErrorData_static = latestErrorData;
        cacheDataRepository_static = cacheDataRepository;
        List<CacheData> cacheDataList = cacheDataRepository.findAll();
        if (cacheDataList.size() > 0) {
            logger.info("No need query");
            CacheData cacheData = cacheDataList.get(0);
            setDataOffset(cacheData);
            latestErrorData.setFirstErrors(cacheData.getFirstErrors());
            latestErrorData.setCountErrors(cacheData.getCountErrors());
            latestErrorData.setCountDataForPieChart(cacheData.getCountDataForPieChart());
        } else {
            logger.info("Need query");
            setLatestErrorData();
            setDataForPieChart();
        }
    }

    private void setDataOffset(CacheData cacheData) {
        Instant dateTo = Instant.now();
        Instant dateFrom = cacheData.getCreatedDate();
        long toYear = dateUtil.getYearFromInstant(dateTo);
        long fromYear = dateUtil.getYearFromInstant(dateFrom);
        List<Signal> signalList = signalRepository
            .findAllByDataType_NameNotInAndIsDeletedFalse
                (Arrays.asList(SignalConstants.DATA_TYPE_INTENSIVE, SignalConstants.DATA_TYPE_CATALOG));
        List<String> signalNameList = signalList
            .parallelStream()
            .map(Signal::getName)
            .collect(Collectors.toList());
//        long countTime = System.currentTimeMillis();
        //region Query for latest error
        List<SignalErrorDTO> result = latestErrorAggregation(dateTo, dateFrom, toYear, fromYear, signalNameList);
        result.forEach(item -> {
            Integer latestErrorCount = cacheData.getCountErrors().get(item.getFirstOccurrence().getThreshold().getId());
            cacheData.getCountErrors().replace(item.getFirstOccurrence().getThreshold().getId(),
                latestErrorCount + Math.toIntExact(item.getTotalOccurrence()));
        });
        //endregion
//        logger.info("Time for query latest error: " + (System.currentTimeMillis() - countTime) + " ms");
//        countTime = System.currentTimeMillis();
        //region Query for pie chart
        List<SignalErrorDTO> result2 = pieChartAggregation(dateTo, dateFrom, toYear, fromYear, signalNameList);
        result2.forEach(
            dto -> {
                Integer pieErrorCount = cacheData.getCountDataForPieChart().get(dto.getFirstOccurrence().getThreshold().getId());
                cacheData.getCountDataForPieChart().replace(dto.getFirstOccurrence().getThreshold().getId(),
                    pieErrorCount + Math.toIntExact(dto.getTotalOccurrence()));
            }
        );
        //endregion
//        logger.info("Time for query latest error: " + (System.currentTimeMillis() - countTime) + " ms");
    }

    private void setLatestErrorData() {
        Instant dateTo = Instant.now();
        Instant dateFrom = dateUtil.minusTimestampMonth(dateTo);
        long toYear = dateUtil.getYearFromInstant(dateTo);
        long fromYear = dateUtil.getYearFromInstant(dateFrom);
        List<Signal> signalList = signalRepository
            .findAllByDataType_NameNotInAndIsDeletedFalse
                (Arrays.asList(SignalConstants.DATA_TYPE_INTENSIVE, SignalConstants.DATA_TYPE_CATALOG));
        List<String> signalNameList = signalList
            .parallelStream()
            .map(Signal::getName)
            .collect(Collectors.toList());
        List<SignalErrorDTO> result = latestErrorAggregation(dateTo, dateFrom, toYear, fromYear, signalNameList);
        result.forEach(
            dto -> {
                Optional<Signal> sig = signalList
                    .stream()
                    .filter(item -> item.getName().equalsIgnoreCase(dto.getName()))
                    .findFirst();
                if (sig.isPresent()) {
                    latestErrorData.getFirstErrors().put(dto.getFirstOccurrence().getThreshold().getId(), dto.getFirstOccurrence());
                    latestErrorData.getCountErrors().put(dto.getFirstOccurrence().getThreshold().getId(), Math.toIntExact(dto.getTotalOccurrence()));
                }
            }
        );
    }

    private void setDataForPieChart() {
        Instant dateTo = Instant.now();
        Instant dateFrom = dateUtil.minusTimestampMonth(dateTo);
        long toYear = dateUtil.getYearFromInstant(dateTo);
        long fromYear = dateUtil.getYearFromInstant(dateFrom);
        List<Signal> signalList = signalRepository
            .findAllByDataType_NameNotInAndIsDeletedFalse
                (Arrays.asList(SignalConstants.DATA_TYPE_INTENSIVE, SignalConstants.DATA_TYPE_CATALOG));
        List<String> signalNameList = signalList
            .parallelStream()
            .map(Signal::getName)
            .collect(Collectors.toList());
        List<SignalErrorDTO> result = pieChartAggregation(dateTo, dateFrom, toYear, fromYear, signalNameList);
        result.forEach(
            dto -> {
                Optional<Signal> sig = signalList
                    .stream()
                    .filter(item -> item.getName().equalsIgnoreCase(dto.getName()))
                    .findFirst();
                if (sig.isPresent()) {
                    latestErrorData.getCountDataForPieChart()
                        .put(dto.getFirstOccurrence().getThreshold().getId(), Math.toIntExact(dto.getTotalOccurrence()));
                }
            }
        );
    }

    public static RcmLatestErrorData latestErrorData_static;

    public static CacheDataRepository cacheDataRepository_static;

    public static void saveDataBeforeShutDown() {
        cacheDataRepository_static.deleteAll();
        CacheData cacheData = new CacheData();
        cacheData.setFirstErrors(latestErrorData_static.getFirstErrors());
        cacheData.setCountErrors(latestErrorData_static.getCountErrors());
        cacheData.setCountDataForPieChart(latestErrorData_static.getCountDataForPieChart());
        cacheDataRepository_static.save(cacheData);
        System.out.println("=========================Store data completed===========================");
    }

    @PreDestroy
    public void storeData() {
        cacheDataRepository.deleteAll();
        CacheData cacheData = new CacheData();
        cacheData.setFirstErrors(latestErrorData.getFirstErrors());
        cacheData.setCountErrors(latestErrorData.getCountErrors());
        cacheData.setCountDataForPieChart(latestErrorData.getCountDataForPieChart());
        cacheDataRepository.save(cacheData);
        logger.debug("=========================Store data completed===========================");
    }


    private List<SignalErrorDTO> pieChartAggregation(Instant dateTo, Instant dateFrom, long toYear, long fromYear, List<String> signalNameList) {
        TypedAggregation<SignalError> aggregation = newAggregation(SignalError.class,
            sort(timestampSort.ascending()),
            match(Criteria.where("name").in(signalNameList)),
            match(Criteria.where("timestamp").gte(dateFrom)),
            match(Criteria.where("timestamp").lte(dateTo)),
            match(Criteria.where("order").gte(fromYear)),
            match(Criteria.where("order").lte(toYear)),
            group("name", "threshold.level")
                .first("$$ROOT").as("firstOccurrence")
                .count().as("totalOccurrence")
        );
        AggregationResults<SignalErrorDTO> aggregationResults = mongoTemplate.aggregate(aggregation,
            SignalError.class, SignalErrorDTO.class);
        return aggregationResults.getMappedResults();
    }


    private List<SignalErrorDTO> latestErrorAggregation(Instant dateTo, Instant dateFrom, long toYear, long fromYear, List<String> signalNameList) {
        TypedAggregation<SignalError> aggregation = newAggregation(SignalError.class,
            sort(timestampSort.ascending()),
            match(Criteria.where("ack").is(false)),
            match(Criteria.where("name").in(signalNameList)),
            match(Criteria.where("timestamp").gte(dateFrom)),
            match(Criteria.where("timestamp").lte(dateTo)),
            match(Criteria.where("order").gte(fromYear)),
            match(Criteria.where("order").lte(toYear)),
            group("name", "threshold.level")
                .first("$$ROOT").as("firstOccurrence")
                .count().as("totalOccurrence")
        );
        AggregationResults<SignalErrorDTO> aggregationResults = mongoTemplate.aggregate(aggregation,
            SignalError.class, SignalErrorDTO.class);
        return aggregationResults.getMappedResults();
    }
}
