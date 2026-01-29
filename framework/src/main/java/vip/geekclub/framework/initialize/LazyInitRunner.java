package vip.geekclub.framework.initialize;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class LazyInitRunner implements ApplicationRunner {
    private final List<InitTask> initTasks;

    @Async
    @Override
    public void run(ApplicationArguments args) throws Exception {

        for (InitTask initTask : initTasks) {
            initTask.initialize();
        }
    }
}
