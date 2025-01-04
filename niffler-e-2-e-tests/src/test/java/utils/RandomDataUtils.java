package utils;

import com.github.javafaker.Faker;

public class RandomDataUtils {

    static Faker faker = new Faker();
    public static String randomUsername = faker.name().username();
    public static String randomName = faker.name().firstName();
    public static String randomSurname = faker.name().lastName();
    public static String randomCategoryName = faker.food().vegetable();
    public static String randomPassword = faker.number().digits(5);

    public static String randomSentence(int wordsCount) {
        return String.join(" ", faker.lorem().words(wordsCount));
    }
}
