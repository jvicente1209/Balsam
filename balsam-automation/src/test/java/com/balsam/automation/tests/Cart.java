package com.balsam.automation.tests;


import org.junit.jupiter.api.Test;

import com.balsam.automation.core.BaseTest;


public class Cart extends BaseTest {
    
    @Test
    public void removeItemFromCart() {
        // Verify that the home page is loaded
        closeCookieBannerIfPresent();
        verifyElementIsDisplayed(Locators.HOME_HEADER);
        enterText(Locators.HOME_SEARCH_BAR, "Christmas tree");
        clickElement(Locators.HOME_SEARCH_BTN);
        getNthElementByXpath(Locators.HOME_RESULTS, 3).click();
        // Plan to add this to a json file unfortunately lack of time due to a family emergency
        scrollIntoView("//Span[text()='7.5']");
        clickElement("//Span[text()='7.5']");
        scrollIntoView("//Span[text()='Full Tree']");
        clickElement("//Span[text()='Full Tree']");
        scrollIntoView("//Span[text()='LED Clear Lights']");
        clickElement("//Span[text()='LED Clear Lights']");
        scrollIntoView(Locators.HOME_ADD_TO_CRT_BTN);
        clickElement(Locators.HOME_ADD_TO_CRT_BTN);
        verifyElementIsDisplayed(Locators.CART_VIEW_BTN);
        clickElement(Locators.CART_VIEW_BTN);
        verifyElementIsDisplayed(Locators.CART_DELETE_BTN);
        scrollIntoView(Locators.CART_DELETE_BTN);
        clickIfExists(Locators.CART_POPUP_NO_BTN);
        clickIfExists(Locators.CART_POPUP_CLOSE_BTN);
        clickElement(Locators.CART_DELETE_BTN);
        scrollIntoView(Locators.CART_REMOVED_CONFIRMATION);
        verifyElementIsDisplayed(Locators.CART_REMOVED_CONFIRMATION);
    }
}