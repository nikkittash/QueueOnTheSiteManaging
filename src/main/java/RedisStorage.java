import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisConnectionException;
import org.redisson.config.Config;

import java.util.Date;

import static java.lang.System.out;

    public class RedisStorage {

        private RedissonClient redisson;

        private RKeys rKeys;

        private RScoredSortedSet<String> Users;

        private final static String KEY = "USERS";

        private double getTs() {
            return new Date().getTime() / 1000;
        }

        private double getNextTs() {
            return Users.firstScore() + 1000;
        }

        void init() {
            Config config = new Config();
            config.useSingleServer().setAddress("redis://127.0.0.1:6379");
            try {
                redisson = Redisson.create(config);
            } catch (RedisConnectionException Exc) {
                out.println("Не удалось подключиться к Redis");
                out.println(Exc.getMessage());
            }
            rKeys = redisson.getKeys();
            Users = redisson.getScoredSortedSet(KEY);
            rKeys.delete(KEY);
        }

        void shutdown() {
            redisson.shutdown();
        }

        void addUser(int user_id)
        {
            Users.add(getTs(), String.valueOf(user_id));
        }

        String showUser() {
            return Users.first();
        }

        public void ToBegin(String user) {
            Users.addScore(user, getNextTs());
        }

        public void ToEnd(String user) {
            Users.addScore(user, getTs());
        }

        public String getRandom(int i) {
            i -= 1;
            int user = (int) (Math.random() * ++i) + 1;
            return Integer.toString(user);
        }
}
