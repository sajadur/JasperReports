package com.springboot.jasperreports.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.springboot.jasperreports.model.Employee;
import com.springboot.jasperreports.service.EmployeeService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/api")
public class EmployeeController {

	final Logger log = LoggerFactory.getLogger(this.getClass());
	final ModelAndView model = new ModelAndView();

	// @Autowired annotation provides the automatic dependency injection.
	@Autowired
	EmployeeService eservice;

	// Method to display the index page of the application.
	@GetMapping(value= "/welcome")
	public ModelAndView index() {
		log.info("Showing the welcome page.");
		model.setViewName("welcome");
		return model;
	}

	// Method to create the pdf report via jasper framework.
	@GetMapping(value = "/view")
	public ModelAndView viewReport(HttpServletResponse httpResponse) {
		log.info("Preparing the pdf report via jasper.");
		try {
			createPdfReport(eservice.findAll(), httpResponse);
			log.info("File successfully saved at the given path.");
		} catch (final Exception e) {
			log.error("Some error has occured while preparing the employee pdf report.");
			e.printStackTrace();
		}
		// Returning the view name as the index page for ease.
		model.setViewName("welcome");
		return model;
	}

	// Method to create the pdf file using the employee list datasource.
	private void createPdfReport(final List<Employee> employees, HttpServletResponse httpResponse) throws JRException {
		// Fetching the .jrxml file from the resources folder.
//		final InputStream stream = this.getClass().getResourceAsStream("/Juvenile.jasper");

		final InputStream stream1 = this.getClass().getResourceAsStream("/test1.jrxml");


		// Compile the Jasper report from .jrxml to .japser
		final JasperReport report = JasperCompileManager.compileReport(stream1);

		// Fetching the employees from the data source.
		final JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(employees);

		// Adding the additional parameters to the pdf.
		final Map<String, Object> parameters = new HashMap<>();
		parameters.put("createdBy", "javacodegeek.com");

		// Filling the report with the employee data and additional parameters information.
//		final JasperPrint print = JasperFillManager.fillReport(stream, parameters, source);




		final InputStream stream = this.getClass().getResourceAsStream("/test1.jrxml");

		JasperDesign jasDesign = JRXmlLoader.load(stream);
		JasperReport jasReport = JasperCompileManager.compileReport(jasDesign);
		JasperPrint jasPrint = JasperFillManager.fillReport(jasReport, parameters, source);
//		JasperViewer.viewReport(jasPrint,false);


//		print.

		// Users can change as per their project requrirements or can take it as request input requirement.
		// For simplicity, this tutorial will automatically place the file under the "c:" drive.
		// If users want to download the pdf file on the browser, then they need to use the "Content-Disposition" technique.
		final String filePath = "\\";
		// Export the report to a PDF file.
//		JasperExportManager.exportReportToPdfFile(print, filePath + "Employee_report.pdf");
		writeWithProperFormat(httpResponse, jasPrint);
	}

	public void writeWithProperFormat(HttpServletResponse httpResponse, JasperPrint print) {
		OutputStream out = null;
		try {
			out = httpResponse.getOutputStream();
			httpResponse.setContentType("application/pdf");
			JasperExportManager.exportReportToPdfStream(print, out);
//			reportBuilder.toPdf(out);

			/*if (ExportType.PDF.getCode().equalsIgnoreCase(exportTye)) {
				httpResponse.setContentType("application/pdf");
				reportBuilder.toPdf(out);
			} else if (ExportType.EXCEL.getCode().equalsIgnoreCase(exportTye)) {
				httpResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				reportBuilder.toXlsx(out);
			}*/
		} catch (RemoteException ex) {
			httpResponse.setStatus(httpResponse.SC_SERVICE_UNAVAILABLE);
		} catch (Exception ex) {
			httpResponse.setStatus(httpResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
//                errorLogger.error(e.getMessage());
			}
		}
	}
}
