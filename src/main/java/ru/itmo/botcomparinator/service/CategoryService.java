package ru.itmo.botcomparinator.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CategoryService {
    Map<String, String> categories = new HashMap<>();

    public void saveCategory(String chatId, String category) {
        categories.put(chatId, category);
    }

    public boolean isCategorySelected(String chatId) {
        return categories.get(chatId) != null;
    }

    public void deleteCategory(String chatId) {
        categories.remove(chatId);
    }

    public String getCategory(String chatId) {
        return categories.get(chatId);
    }
}
