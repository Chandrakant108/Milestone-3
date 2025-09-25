package org.automation.reports;

import java.io.FileWriter;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HtmlReportGenerator {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/automation_tests";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Ck@709136";

    public static void generateReport() throws Exception {
        String fileName = "artifacts/reports/TestReport_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".html";

        int passCount = 0, failCount = 0, skipCount = 0;
        StringBuilder tableRows = new StringBuilder();
        StringBuilder timeData = new StringBuilder(); // For optional execution time chart

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM execution_log ORDER BY execution_time ASC")) {

            while (rs.next()) {
                String status = rs.getString("status");
                String statusClass = "status-skip";
                if ("PASS".equalsIgnoreCase(status)) { statusClass = "status-pass"; passCount++; }
                else if ("FAIL".equalsIgnoreCase(status)) { statusClass = "status-fail"; failCount++; }
                else { skipCount++; }

                tableRows.append("<tr>")
                        .append("<td>").append(rs.getInt("id")).append("</td>")
                        .append("<td>").append(rs.getString("test_name")).append("</td>")
                        .append("<td><span class='").append(statusClass).append("'>").append(status).append("</span></td>")
                        .append("<td>").append(rs.getString("test_type")).append("</td>")
                        .append("<td>").append(rs.getString("us_id")).append("</td>")
                        .append("<td>").append(rs.getString("tc_id")).append("</td>")
                        .append("<td>").append(rs.getString("execution_time")).append("</td>")
                        .append("</tr>");

                timeData.append("\"").append(rs.getString("execution_time")).append("\",");
            }
        }

        int total = passCount + failCount + skipCount;
        double successRate = total == 0 ? 0 : ((double) passCount / total) * 100;

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'>");
            writer.write("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            writer.write("<title>Automation Test Report</title>");
            writer.write("<script src='https://cdn.jsdelivr.net/npm/chart.js'></script>");
            writer.write("<style>");
            writer.write("body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; background: #f0f2f5; }");
            writer.write(".container { max-width: 1400px; margin: auto; padding: 20px; }");
            writer.write("h1 { text-align: center; color: #34495e; margin-bottom: 30px; }");
            writer.write(".summary { display: flex; justify-content: space-around; flex-wrap: wrap; margin-bottom: 30px; }");
            writer.write(".card { background: #fff; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); padding: 20px; margin: 10px; flex: 1 1 200px; text-align: center; }");
            writer.write(".card h2 { margin: 0; font-size: 2rem; color: #2c3e50; }");
            writer.write(".card p { margin: 5px 0 0 0; color: #7f8c8d; font-weight: bold; }");
            writer.write("table { width: 100%; border-collapse: collapse; margin-top: 20px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); }");
            writer.write("th, td { padding: 12px 15px; text-align: center; }");
            writer.write("th { background: linear-gradient(90deg, #2980b9, #6dd5fa); color: white; }");
            writer.write("tr:nth-child(even) { background: #ecf0f1; }");
            writer.write("tr:hover { background: #d6eaf8; }");
            writer.write(".status-pass { background: #2ecc71; color: white; padding: 5px 10px; border-radius: 20px; font-weight: bold; }");
            writer.write(".status-fail { background: #e74c3c; color: white; padding: 5px 10px; border-radius: 20px; font-weight: bold; }");
            writer.write(".status-skip { background: #f39c12; color: white; padding: 5px 10px; border-radius: 20px; font-weight: bold; }");
            writer.write("@media (max-width: 768px) { .summary { flex-direction: column; align-items: center; } }");
            writer.write("</style></head><body>");
            writer.write("<div class='container'>");
            writer.write("<h1>Automation Test Report</h1>");

            // Summary Cards
            writer.write("<div class='summary'>");
            writer.write("<div class='card'><h2>" + total + "</h2><p>Total Tests</p></div>");
            writer.write("<div class='card'><h2>" + passCount + "</h2><p>Passed</p></div>");
            writer.write("<div class='card'><h2>" + failCount + "</h2><p>Failed</p></div>");
            writer.write("<div class='card'><h2>" + skipCount + "</h2><p>Skipped</p></div>");
            writer.write("<div class='card'><h2>" + String.format("%.2f", successRate) + "%</h2><p>Success Rate</p></div>");
            writer.write("</div>");

            // Charts
            writer.write("<canvas id='statusChart' width='400' height='200'></canvas>");
            writer.write("<script>");
            writer.write("new Chart(document.getElementById('statusChart').getContext('2d'), {");
            writer.write("type: 'pie',");
            writer.write("data: { labels: ['PASS','FAIL','SKIP'], datasets: [{");
            writer.write("data: [" + passCount + "," + failCount + "," + skipCount + "],");
            writer.write("backgroundColor: ['#2ecc71','#e74c3c','#f39c12'] } ] },");
            writer.write("options: { responsive: true, plugins: { legend: { position: 'bottom' } } }");
            writer.write("});");
            writer.write("</script>");

            // Table
            writer.write("<table>");
            writer.write("<tr><th>ID</th><th>Test Name</th><th>Status</th><th>Type</th>"
                    + "<th>US_ID</th><th>TC_ID</th><th>Execution Time</th></tr>");
            writer.write(tableRows.toString());
            writer.write("</table>");

            writer.write("</div></body></html>");
        }

        System.out.println("✅ Professional HTML report generated: " + fileName);
    }

    public static void generateHtmlReport() throws Exception {
        generateReport();
    }
}
