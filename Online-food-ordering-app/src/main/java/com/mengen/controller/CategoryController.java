package com.mengen.controller;

import com.mengen.model.Category;
import com.mengen.model.User;
import com.mengen.service.CategoryService;
import com.mengen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;


    private final UserService userService;

    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @PostMapping("/admin/category")
    public ResponseEntity<Category> categoryCategory(@RequestBody Category category,
                                                     @RequestHeader("Authorization") String jwt) throws Exception {
        User userByJwtToken = userService.findUserByJwtToken(jwt);

        Category createCategory = categoryService.createCategory(category.getName(), userByJwtToken.getId());
        return new ResponseEntity<>(createCategory, HttpStatus.CREATED);
    }

    @GetMapping("/category/restaurant")
    public ResponseEntity< List<Category>> getRestaurantCategory(@RequestBody Category category,
                                                          @RequestHeader("Authorization") String jwt) throws Exception {
        User userByJwtToken = userService.findUserByJwtToken(jwt);
        List<Category> createCategory = categoryService.findCategoryByRestaurantId(category.getId());
        return new ResponseEntity<>(createCategory, HttpStatus.CREATED);
    }
}
