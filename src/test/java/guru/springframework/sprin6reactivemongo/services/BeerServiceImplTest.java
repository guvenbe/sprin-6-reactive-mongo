package guru.springframework.sprin6reactivemongo.services;

import guru.springframework.sprin6reactivemongo.domain.Beer;
import guru.springframework.sprin6reactivemongo.mappers.BeerMapper;
import guru.springframework.sprin6reactivemongo.model.BeerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
class BeerServiceImplTest {

    @Autowired
    BeerService beerService;

    @Autowired
    BeerMapper beerMapper;

    BeerDTO beerDto;
    @BeforeEach
    void setUp() {
        beerDto = beerMapper.beerToBeerDto(getTestBeerDto());
    }

    @Test
    @DisplayName("Test save  Beer using Subscriber")
    void saveBeerTest()  {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<BeerDTO> atomicDto = new AtomicReference<>();
        Mono<BeerDTO> savedMono = beerService.saveBeer(Mono.just(beerDto));

        savedMono.subscribe(savedDto ->{
            System.out.println("BeerId=" + savedDto.getId());
            atomicBoolean.set(true);
            atomicDto.set(savedDto);
        });

        //Thread.sleep(1000l);
        await().untilTrue(atomicBoolean);

        BeerDTO persistedDto = atomicDto.get();
        assertThat(persistedDto).isNotNull();
        assertThat(persistedDto.getId()).isNotNull();

    }

    @Test
    @DisplayName("Test Save Beer Using Block")
    void testSaveBeerUseBlock() {
//        BeerDTO savedDto = beerService.saveBeer(Mono.just(getTestBeerDto())).block();
    }

    public static Beer getTestBeerDto(){
        return Beer.builder()
                .beerName("space Dust")
                .beerStyle("IPA")
                .price(BigDecimal.TEN)
                .quantityOnHand(12)
                .upc("123312")
                .build();
    }
}