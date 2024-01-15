package com.gamecrew.gamecrew_project.global.faker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class DummyDataGenerator {
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String password;


// post 더미데이터
//    @Bean
//    public void generateData(){
//        try(Connection conn = DriverManager.getConnection(url, user, password)) {
//            Faker faker = new Faker();
//
//            String query = "INSERT INTO post (created_at, modified_at, category, content, current_num, post_view_count, title, total_number, view, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//
//            PreparedStatement pstmt = conn.prepareStatement(query);
//
//            CategoryType[] categories = CategoryType.values();
//
//
//            for (int i = 0; i < 94000; i++) {
//                Date startDate = new SimpleDateFormat("yyyy.MM.dd").parse("2022.11.23");
//                Date endDate = new SimpleDateFormat("yyyy.MM.dd").parse("2023.12.21");
//                java.util.Date createdDate = faker.date().between(startDate, endDate);
//                pstmt.setTimestamp(1, new java.sql.Timestamp(createdDate.getTime()));
//
//                // 생성된 날짜 이후의 날짜를 수정된 날짜로 설정합니다.
//                java.util.Date modifiedDate = faker.date().future(365, TimeUnit.DAYS, createdDate);
//                pstmt.setTimestamp(2, new java.sql.Timestamp(modifiedDate.getTime()));
//
//
//                CategoryType randomCategory = categories[faker.random().nextInt(categories.length)]; // 무작위로 카테고리를 선택합니다.
//                pstmt.setString(3, randomCategory.name()); // 선택한 카테고리의 이름을 설정합니다.
//
//                pstmt.setString(4, faker.lorem().sentence());
//
//                //total_num
//                long totalNumber = (long)faker.random().nextInt(1, 25); // 1~25 사이의 랜덤한 정수를 생성하고 long으로 변환합니다.
//                pstmt.setLong(8, totalNumber);
//
//                //current_num
//                pstmt.setLong(5, (long)faker.random().nextInt((int)totalNumber)); // 0~totalNumber 사이의 랜덤한 정수를 생성하고 long으로 변환합니다.
//
//                //post_view
//                pstmt.setLong(6, (long)faker.random().nextInt(10000));
//
//                //title
//                pstmt.setString(7, faker.lorem().sentence());
//
//                //view
//                pstmt.setLong(9, (long)faker.random().nextInt(10000));
//
//                //user_id
//                pstmt.setLong(10, 1L + faker.random().nextLong(1000));
//
//                pstmt.executeUpdate();
//
//            }
//        } catch (SQLException | ParseException e) {
//            throw new RuntimeException(e);
//        }
//    }


//    @Bean
//    public void generateData(){
//        try(Connection conn = DriverManager.getConnection(url, user, password)) {
//            Faker faker = new Faker();
//
//            String query = "INSERT INTO record_of_ratings (recorded_at, enjoyable, evaluator, gaming_skill, manner, participation, sociability, total_rating, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
//
//            PreparedStatement pstmt = conn.prepareStatement(query);
//
//            for (int i = 0; i < 200000; i++) {
//                //recorded_at
//                Date startDate = new SimpleDateFormat("yyyy.MM.dd").parse("2022.11.23");
//                Date endDate = new SimpleDateFormat("yyyy.MM.dd").parse("2023.12.21");
//                java.util.Date recordedDate = faker.date().between(startDate, endDate);
//                pstmt.setTimestamp(1, new java.sql.Timestamp(recordedDate.getTime()));
//
//                //enjoyable
//                int enjoyable = faker.random().nextInt(10);
//                pstmt.setInt(2, enjoyable);
//
//                //evaluator
//                long evaluator = 1L + faker.random().nextLong(1000);
//                pstmt.setLong(3, evaluator);
//
//                //gaming_skill
//                int gaming_skill = faker.random().nextInt(10);
//                pstmt.setInt(4, gaming_skill);
//
//                //manner
//                int manner = faker.random().nextInt(10);
//                pstmt.setInt(5, manner);
//
//                //participation
//                int participation = faker.random().nextInt(10);
//                pstmt.setInt(6, participation);
//
//                //sociability
//                int sociability = faker.random().nextInt(10);
//                pstmt.setInt(7, sociability);
//
//                //total_rating
//                double total_rating = (enjoyable + gaming_skill + manner + participation + sociability) / 5.0;
//                total_rating = Math.round(total_rating * 2) / 2.0; // 소수점 둘째 자리까지 반올림
//                pstmt.setDouble(8, total_rating);
//
//                //user_id - evaluator와 다른 값이 되도록 함
//                long user_id;
//                do {
//                    user_id = 1L + faker.random().nextLong(1000);
//                } while(user_id == evaluator);
//                pstmt.setLong(9, user_id);
//
//                pstmt.executeUpdate();
//            }
//        } catch (SQLException | ParseException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
