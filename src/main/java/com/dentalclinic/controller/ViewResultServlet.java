package com.dentalclinic.controller;

import com.dentalclinic.dao.ExaminationResultDAO;
import com.dentalclinic.model.ExaminationResult;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/ViewResultServlet")
public class ViewResultServlet extends HttpServlet {

    private final transient ExaminationResultDAO resultDAO = new ExaminationResultDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String patientId = request.getParameter("patientId");
        List<ExaminationResult> results = resultDAO.getByPatientID(Integer.parseInt(patientId));
        request.setAttribute("results", results);
        request.getRequestDispatcher("/view_result.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Fix: bỏ parameter không dùng — gọi thẳng doGet thay vì method phụ có param thừa
        doGet(request, response);
    }

    // Fix: loại bỏ unused parameter — phương thức nội bộ chỉ nhận đúng những gì cần
    private List<ExaminationResult> loadResults(int patientId) {
        return resultDAO.getByPatientID(patientId);
    }
}
