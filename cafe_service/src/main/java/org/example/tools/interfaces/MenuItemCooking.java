package org.example.tools.interfaces;

import org.example.dao.FoodOrderDao;
import org.example.dao.MenuItemDao;
import org.example.dao.OrderMenuItemDao;
import org.example.entities.FoodOrder;
import org.example.entities.MenuItem;
import org.example.entities.OrderMenuItem;

public class MenuItemCooking implements Runnable {
    private final Integer menuItemId;
    private final Integer foodOrderId;

    private final FoodOrderDao foodOrderDao;

    private final MenuItemDao menuItemDao;

    private final OrderMenuItemDao orderMenuItemDao = new OrderMenuItemDao(OrderMenuItem.class);

    public MenuItemCooking(Integer menuItemId, Integer foodOrderId, MenuItemDao menuItemDao, FoodOrderDao foodOrderDao) {
        this.menuItemId = menuItemId;
        this.foodOrderId = foodOrderId;
        this.menuItemDao = menuItemDao;
        this.foodOrderDao = foodOrderDao;
    }

    private boolean isCanceled(FoodOrder foodOrder) {
        return (foodOrder.getStatus().equals("Отменён"));
    }

    public void run() {
        FoodOrder foodOrder = foodOrderDao.findById(foodOrderId);
        MenuItem menuItem = menuItemDao.findById(menuItemId);
        if (isCanceled(foodOrder)) {
            return;
        }
        if (menuItemDao.reserveItem(menuItemId)) {
            foodOrder.setStatus("Готовится");
            foodOrderDao.update(foodOrder);
            OrderMenuItem orderMenuItem = new OrderMenuItem(foodOrder, menuItem, "В процессе");
            orderMenuItemDao.save(orderMenuItem);
            try {
                Thread.sleep(menuItem.getTime() * 1000);
                foodOrder = foodOrderDao.findById(foodOrderId);
                if (isCanceled(foodOrder)) {
                    return;
                }
                orderMenuItem.setStatus("Приготовлено");
                orderMenuItemDao.update(orderMenuItem);
                if (orderMenuItemDao.CountPositions(foodOrderId) == foodOrder.getInd()) {
                    foodOrder.setStatus("Готов");
                    foodOrderDao.update(foodOrder);
                    foodOrderDao.setDoneStatus(foodOrderId);
                }
            } catch (InterruptedException ignored) { }
        } else {
            foodOrder.setStatus("Отменён");
        }
        foodOrderDao.update(foodOrder);
    }
}
