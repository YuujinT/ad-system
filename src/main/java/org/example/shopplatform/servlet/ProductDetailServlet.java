package org.example.shopplatform.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.shopplatform.model.Product;
import org.example.shopplatform.service.ProductService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "productDetailServlet", urlPatterns = "/product/detail")
public class ProductDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productIdParam = request.getParameter("id");
        
        if (productIdParam == null || productIdParam.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "商品ID不能为空");
            return;
        }
        
        try {
            Long productId = Long.parseLong(productIdParam);
            List<Product> allProducts = ProductService.getAllProducts();
            Product product = allProducts.stream()
                    .filter(p -> p.getId().equals(productId))
                    .findFirst()
                    .orElse(null);
            
            if (product == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "未找到指定的商品");
                return;
            }
            
            request.setAttribute("product", product);
            request.setAttribute("categories", ProductService.getCategories());
            request.getRequestDispatcher("/WEB-INF/views/product-detail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效的商品ID格式");
        }
    }
}