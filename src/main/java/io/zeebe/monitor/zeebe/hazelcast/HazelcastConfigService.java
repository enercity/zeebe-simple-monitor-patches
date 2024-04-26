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

  @Autowired private HazelcastConfigRepository hazelcastConfigRepository;
  @Autowired private MeterRegistry meterRegistry;

  public long getLastSequenceNumber() {
    return
        getHazelcastConfig().getSequence();
  }

  @Transactional
  public void saveSequenceNumber(long sequence) {
    HazelcastConfig config = getHazelcastConfig();

    long prev = config.getSequence();

    config.setSequence(sequence);

    hazelcastConfigRepository.save(config);

    Counter.builder("zeebemonitor_importer_ringbuffer_sequences_read").
            description("number of items read from Hazelcast's ringbuffer (sequence counter)").
            register(meterRegistry).
            increment(sequence - prev);
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
