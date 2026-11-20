package io.zeebe.monitor.repository;

import static org.assertj.core.api.Assertions.assertThat;

import io.zeebe.monitor.entity.ElementInstanceEntity;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ElementInstanceRepositoryTest extends ZeebeRepositoryTest {

  @Autowired private ElementInstanceRepository elementInstanceRepository;

  @Test
  public void JPA_will_automatically_update_the_ID_attribute() {
    // given
    ElementInstanceEntity elementInstance = createElementInstance(123, 456);

    // when
    elementInstanceRepository.save(elementInstance);

    // then
    assertThat(elementInstance.getId()).isEqualTo("123-456");
  }

  @Test
  public void variable_can_be_retrieved_by_transient_ID() {
    // given
    ElementInstanceEntity elementInstance = createElementInstance(123, 456);

    // when
    elementInstanceRepository.save(elementInstance);

    // then
    Optional<ElementInstanceEntity> entity = elementInstanceRepository.findById("123-456");
    assertThat(entity).isPresent();
  }

  private ElementInstanceEntity createElementInstance(int partitionId, long position) {
    ElementInstanceEntity elementInstance = new ElementInstanceEntity();
    elementInstance.setPartitionId(partitionId);
    elementInstance.setPosition(position);
    return elementInstance;
  }

  @Test
  public void should_delete_by_process_instance_key_in() {
    // given
    ElementInstanceEntity elementInstance1 = createElementInstance(123, 456);
    elementInstance1.setProcessInstanceKey(1001L);

    ElementInstanceEntity elementInstance2 = createElementInstance(234, 567);
    elementInstance2.setProcessInstanceKey(1002L);

    ElementInstanceEntity elementInstance3 = createElementInstance(345, 678);
    elementInstance3.setProcessInstanceKey(1003L);
    elementInstanceRepository.saveAll(Arrays.asList(elementInstance1, elementInstance2, elementInstance3));

    // when
    List<Long> keysToDelete = Arrays.asList(1001L, 1003L);
    elementInstanceRepository.deleteByProcessInstanceKeyIn(keysToDelete);

    // then
    Iterable<ElementInstanceEntity> remainingInstances = elementInstanceRepository.findAll();
    assertThat(remainingInstances).hasSize(1);
    assertThat(remainingInstances.iterator().next().getProcessInstanceKey()).isEqualTo(1002L);
  }
}
