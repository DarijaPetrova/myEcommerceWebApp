package com.example.proekt.web.controller;

import com.example.proekt.model.Category;
import com.example.proekt.model.Product;
import com.example.proekt.repository.CategoryRepository;
import com.example.proekt.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;


   @GetMapping
   @Secured("ROLE_ADMIN")
    public String getCategories(Model model){
        List<Category> categories=this.categoryRepository.findAll();
        model.addAttribute("categories",categories);
        return "categories";
    }


    @GetMapping("/add-new-category")
    @Secured("ROLE_ADMIN")
    public String addNewCategoryPage(Model model){
        model.addAttribute("category", new Category());
        return "add-category";
    }


    @PostMapping
    @Secured("ROLE_ADMIN")
    public String saveCategory(Category category){
        this.categoryRepository.save(category);
        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    @Secured("ROLE_ADMIN")
    public String editCategory(@PathVariable Long id, Model model){
       try {
           Category category = this.categoryRepository.findById(id).orElseThrow();
           model.addAttribute("category", category);

           return "add-category";
       }
       catch (RuntimeException ex){
           return "redirect:/categories?error=Ne postoi takva kategorija.";
       }

    }

    @PostMapping("/{id}/delete")
    @Secured("ROLE_ADMIN")
    public String deleteCategory(@PathVariable Long id){
        Category category = this.categoryRepository.findById(id).orElseThrow();
        if(category.getProducts().size()!=0){ // Dokolku vo kategorijata ima produkti da dava error, dokolku nema go brise
            return "redirect:/categories?error=Ne moze da se izbrise ovaa kategorija bidejki ima produkti vo nea.";
        }
        else {
            this.categoryRepository.deleteById(id);
            return "redirect:/categories";
        }
    }

}
