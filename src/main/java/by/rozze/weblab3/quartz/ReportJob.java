package by.rozze.weblab3.quartz;

import by.rozze.weblab3.mail.MailService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.CDI;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class ReportJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        try {
            String email = context.getMergedJobDataMap().getString("email");
            Long pointsCount = context.getMergedJobDataMap().getLong("pointsCount");
            MailService mailService = CDI.current().select(MailService.class).get();
            String time = ZonedDateTime.now(ZoneId.of("Europe/Moscow"))
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            mailService.sendReport(email, pointsCount, time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}