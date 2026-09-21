import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BikeTest {

    @Test
    void testBikeIsTurnedOnWhenPressed() {
        Bike bike = new Bike();
        assertTrue(bike.powerOn());
    }

    @Test
    void testBikeisTurnedOffWhenPressed() {
        Bike bike = new Bike();
        assertFalse(bike.powerOff());
    }

    @Test
    void testWhenBikeIsInGearOneItAcceleratesInIncrementsOfOne() {
        Bike bike = new Bike();
        bike.setGear(15);
        bike.accelerate(bike.getGear());

        assertEquals(16, bike.getSpeed());
    }

    @Test
    void testWhenBikeIsInGearTwoItAcceleratesInIncrementsOfTwo() {
        Bike bike = new Bike();
        bike.setGear(24);
        bike.accelerate(bike.getGear());
        assertEquals(26, bike.getSpeed());
    }

    @Test
    void testWhenTheBikeIsOnGearThreeItAcceleratesInIncrementOf3() {
        Bike bike = new Bike();
        bike.setGear(35);
        bike.accelerate(bike.getGear());

        assertEquals(38, bike.getSpeed());
    }

    @Test
    void testWhenBikeIsInGearFourItAcceleratesInIncrementOfFour() {
        Bike bike = new Bike();
        bike.setGear(44);
        bike.accelerate(bike.getGear());

        assertEquals(48, bike.getSpeed());
    }

    @Test
    void testWhenBikeIsInGearOneItDeceleratesInDecrementsOfOne() {
        Bike bike = new Bike();
        bike.setGear(15);
        bike.decelerate(bike.getGear());

        assertEquals(14, bike.getSpeed());
    }

    @Test
    void testWhenBikeIsInGearTwoItDeceleratesInDecrementsOfTwo() {
        Bike bike = new Bike();
        bike.setGear(24);
        bike.decelerate(bike.getGear());

        assertEquals(22, bike.getSpeed());
    }

    @Test
    void testWhenBikeIsInGearThreeItDeceleratesInDecrementsOfThree() {
        Bike bike = new Bike();
        bike.setGear(35);
        bike.decelerate(bike.getGear());

        assertEquals(32, bike.getSpeed());
    }

    @Test
    void testWhenBikeIsInGearFourItDeceleratesInDecrementsOfFour() {
        Bike bike = new Bike();
        bike.setGear(44);
        bike.decelerate(bike.getGear());

        assertEquals(40, bike.getSpeed());
    }

    @Test
    void testThatBikeStartsInGearOne() {
        Bike bike = new Bike();

        assertEquals(1, bike.getGear());
    }

    @Test
    void testThatBikeStartsAtRest() {
        Bike bike = new Bike();

        assertEquals(0, bike.getSpeed());
    }

    @Test
    void testThatGearOneHasAMaxSpeedOfTwenty() {
        Bike bike = new Bike();
        bike.setGear(20);

        assertEquals(1, bike.getGear());

    }

    @Test
    void testThatBikeGoesIntoGearTwoAtSpeedTwentyOne() {
        Bike bike = new Bike();
        bike.setSpeed(21);

        assertEquals(2, bike.getGear());
    }

    @Test
    void testThatGearTwoHasAMaxSpeedOfThirty() {
        Bike bike = new Bike();
        bike.setSpeed(30);

        assertEquals(2, bike.getGear());
    }

    @Test
    void testThatBikeGoesIntoGearThreeAtSpeedThirtyOne() {
        Bike bike = new Bike();
        bike.setSpeed(31);

        assertEquals(3, bike.getGear());
    }

    @Test
    void  testThatGearThreeHasAMaxSpeedOfForty() {
        Bike bike = new Bike();
        bike.setGear(40);

        assertEquals(3, bike.getGear());
    }

    @Test
    void testThatBikeGoesIntoGearFourAtSpeedFortyOneAndAbove() {
        Bike bike = new Bike();
        bike.setSpeed(41);

        assertEquals(4, bike.getGear());
        bike.setSpeed(200);

        assertEquals(4, bike.getGear());
    }









}
