import static java.lang.System.out;

    public class RedisTest {

        private static final int SLEEP = 1000; // 1 секунда

        private static void log(String User) {
            String log = String.format("На главной странице показываем пользователя %s", User);
            out.println(log);
        }

        private static void logPaid(String User) {
            String logPaid = String.format("Пользователь %s оплатил платную услугу", User);
            out.println(logPaid);
        }

        private static int userId = 1;

        public static void main(String[] args) throws InterruptedException {
            RedisStorage redis = new RedisStorage();
            redis.init();
            int i;
            for (i = 0; i < 20; i++) {
                redis.addUser(userId);
                userId++;
            }
            for(int seconds = 0; seconds <= 60;) {
                int a = 0;
                while (a < 20) {
                    Thread.sleep(SLEEP);
                    seconds++;
                    a++;
                    String userNonPaid = redis.showUser();
                    log(userNonPaid);
                    redis.ToEnd(userNonPaid);
                    if(seconds % 10 == 0) {
                        String userPaid = redis.getRandom(i);
                        redis.ToBegin(userPaid);
                        logPaid(userPaid);
                        redis.ToEnd(userPaid);
                    }
                }
                Thread.sleep((3 * SLEEP));
            }
            redis.shutdown();
        }
}
