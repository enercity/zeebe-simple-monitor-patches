package io.zeebe.monitor.zeebe.hazelcast;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.zeebe.monitor.entity.HazelcastConfig;
import io.zeebe.monitor.repository.HazelcastConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class HazelcastConfigService {

  private final HazelcastConfigRepository hazelcastConfigRepository;
  private final Counter sequenceCounter;

  @Autowired
  public HazelcastConfigService(HazelcastConfigRepository hazelcastConfigRepository, MeterRegistry meterRegistry) {
    this.hazelcastConfigRepository = hazelcastConfigRepository;

    sequenceCounter = Counter.builder("zeebemonitor_importer_ringbuffer_sequences_read").
            description("number of items read from Hazelcast's ringbuffer (sequence counter)").
            register(meterRegistry);
  }

  public long getLastSequenceNumber() {
    return getHazelcastConfig().getSequence();
  }

  @Transactional
  public void saveSequenceNumber(long sequence) {
    HazelcastConfig config = getHazelcastConfig();

    long prev = config.getSequence();

    config.setSequence(sequence);

    hazelcastConfigRepository.save(config);

    sequenceCounter.increment(sequence - prev);
  }

  private HazelcastConfig getHazelcastConfig() {
    return hazelcastConfigRepository
            .findById("cfg")
            .orElseGet(
                    () -> {
                      final var config = new HazelcastConfig();
                      config.setId("cfg");
                      config.setSequence(-1);
                      return config;
                    });
  }
}
