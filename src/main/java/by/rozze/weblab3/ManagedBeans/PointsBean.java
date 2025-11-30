package by.rozze.weblab3.ManagedBeans;

import by.rozze.weblab3.Service.AreaCheckService;
import by.rozze.weblab3.Service.ReportScheduler;
import by.rozze.weblab3.hibernate.ResultDao;
import by.rozze.weblab3.model.Result;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Named("pointBean")
@SessionScoped
public class PointsBean implements Serializable {
    @Inject
    private ReportScheduler scheduler;
    private static final Logger log = Logger.getLogger(PointsBean.class.getName());
    private double x;
    private double y;
    private double radius;
    private double graphX;
    private double graphY;
    private String errorMessage;
    private final List<Result> results = new ArrayList<>();
    private String reportEmail = "";
    private double delayHours = 0.016;

    private AreaCheckService areaCheckService = new AreaCheckService();
    private ResultDao resultDao = new ResultDao();

    public ReportScheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(ReportScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        this.errorMessage = null;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        this.errorMessage = null;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
        this.errorMessage = null;
    }

    public double getGraphX() { return graphX; }
    public void setGraphX(double graphX) { this.graphX = graphX; }

    public double getGraphY() { return graphY; }
    public void setGraphY(double graphY) { this.graphY = graphY; }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String submit() {
        if (Double.isNaN(x)) {
            errorMessage = "Выберите значение X!";
            return null;
        }

        if (Double.isNaN(y)) {
            errorMessage = "Выберите значение Y!";
            return null;
        }

        if (y < -3 || y > 3) {
            errorMessage = "Y должен быть между -3 и 3";
            return null;
        }

        if (Double.isNaN(radius)) {
            errorMessage = "Выберите радиус R!";
            return null;
        }

        boolean hit = AreaCheckService.checkHit(x, y, radius);
        Result result = new Result();
        result.setX(x);
        result.setY(y);
        result.setR(radius);
        result.setTimestamp(LocalDateTime.now());
        result.setHit(hit);
        resultDao.save(result);
        addResult(result);
        errorMessage = null;
        return "main?faces-redirect=true";
    }

    public String submitFromGraph() {
        if (Double.isNaN(graphX) || Double.isNaN(graphY)) {
            errorMessage = "Неверные координаты с графика";
            return null;
        }
        this.x = graphX;
        this.y = graphY;
        if (Double.isNaN(radius)) {
            errorMessage = "Выберите радиус R!";
            return null;
        }

        boolean hit = AreaCheckService.checkHit(graphX, graphY, radius);
        Result result = new Result();
        result.setX(graphX);
        result.setY(graphY);
        result.setR(radius);
        result.setTimestamp(LocalDateTime.now());
        result.setHit(hit);

        resultDao.save(result);
        addResult(result);
        errorMessage = null;

        return "main?faces-redirect=true";
    }

    public void clear() {
        this.x = Double.NaN;
        this.y = 0;
        this.radius = Double.NaN;
        this.errorMessage = null;
        this.graphX = Double.NaN;
        this.graphY = Double.NaN;
    }

    public List<Result> getResults() {
        return results;
    }

    public void addResult(Result result) {
        results.add(0, result);
    }

    public AreaCheckService getAreaCheckService() {
        return areaCheckService;
    }

    public void setAreaCheckService(AreaCheckService areaCheckService) {
        this.areaCheckService = areaCheckService;
    }

    public ResultDao getResultDao() {
        return resultDao;
    }

    public void setResultDao(ResultDao resultDao) {
        this.resultDao = resultDao;
    }

    public String getReportEmail() {
        return reportEmail;
    }

    public void setReportEmail(String reportEmail) {
        this.reportEmail = reportEmail;
    }

    public double getDelayHours() { return delayHours; }
    public void setDelayHours(double delayHours) { this.delayHours = delayHours; }

    public void scheduleReport() {
        if (reportEmail == null || reportEmail.isBlank()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Введите email", null));
            return;
        }

        long currentPoints = results.size();

        try {
            scheduler.scheduleReport(reportEmail.trim(), delayHours, currentPoints);

            String when = ZonedDateTime.now(ZoneId.of("Europe/Moscow"))
                    .plusMinutes((long)(delayHours * 60))
                    .format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Письмо с " + currentPoints + " точками придёт в " + when, null));

            reportEmail = "";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка: " + e.getMessage(), null));
        }
    }
}
