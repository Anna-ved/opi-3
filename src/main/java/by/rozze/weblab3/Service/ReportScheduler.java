package by.rozze.weblab3.Service;

import by.rozze.weblab3.quartz.ReportJob;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@ApplicationScoped
public class ReportScheduler {

    private Scheduler scheduler;

    @PostConstruct
    void start() throws SchedulerException {
        scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
    }

    @PreDestroy
    void stop() throws SchedulerException {
        if (scheduler != null) scheduler.shutdown(true);
    }

    public void scheduleReport(String email, double hoursFromNow, long pointsCount)
            throws SchedulerException {

        ZonedDateTime runAt = ZonedDateTime.now(ZoneId.of("Europe/Moscow"))
                .plusMinutes((long)(hoursFromNow * 60));

        JobDetail job = JobBuilder.newJob(ReportJob.class)
                .withIdentity("report_" + System.nanoTime(), "reports")
                .usingJobData("email", email)
                .usingJobData("pointsCount", pointsCount)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .startAt(Date.from(runAt.toInstant()))
                .build();

        scheduler.scheduleJob(job, trigger);
    }
}