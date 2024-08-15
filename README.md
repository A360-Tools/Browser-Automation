# Browser Automation Package

This browser automation package is designed to simplify web automation tasks for Automation Anywhere.This package
retains all the capabilities of the Selenium WebDriver. You can perform actions such as opening the browser, extracting
data, inputting values, clicking elements, and working across iframes as needed for your automation tasks.

## Demo

![Animation](https://github.com/A360-Tools/Browser-Automation/assets/82057278/ceef22da-fc4d-4ff5-81d4-b202887f9afa)

## Highlights

![activites](https://github.com/A360-Tools/Browser-Automation/assets/82057278/75554df4-902a-49db-a525-2cae2bd4ee0f)

### 1. Automatic Webdriver Management

One of the key enhancements of this package is its ability to automatically detect the browser version being used and
download the appropriate webdriver. This eliminates the need for manual configuration and ensures seamless compatibility
across various browser versions.

### 2. Global Session Management

The actions of this package can use a shared session variable, allowing you to reuse the same browser session across
multiple bots. This not only improves efficiency but also reduces resource consumption by avoiding the need to open and
close browser sessions repeatedly.

### 3. Handling JavaScript Alerts

Web applications often employ JavaScript alerts for various purposes. This package includes functionality to handle
JavaScript alerts, ensuring that your automation scripts can interact with web applications that use them.

### 4. Enhanced Navigation Options

Navigation within web applications is made more flexible with options to go back, forward, refresh, and open new tabs.
These navigation features simplify interaction with complex web workflows.

### 5. Improved Table Extraction

The package addresses issues related to table extraction from HTML content. It includes fixes and enhancements that make
it easier to extract data from tables in web pages.

## Important Note for Community Edition Control Room Users

- **Warning**: Please be aware that in the Community Edition Control Room, the system does not automatically set the
  latest version of imported packages as the default. To ensure you are using the latest and fully functional features,
  you must manually change the package version to the latest version in every bot.

## Building the Project

You can build this project using Gradle with the following command:

```bash
gradle clean build :shadowJar
```
