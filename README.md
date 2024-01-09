
# Purpose

Mama M's Kitchen Manager App is a powerful Android application designed to simplify the management of Mama M's Kitchen operations. This app serves as a comprehensive tool for handling menu items, orders, and user interactions.

# Scope

The app encompasses features such as managing menu items, monitoring orders, handling user data, and maintaining communication through push notifications.

# Target Audience

This app is intended for the staff and management of Mama M's Kitchen to efficiently manage their kitchen, menu, and orders.

# Features

# Overview of App Features

**_Menu Management:_** Add, edit, and remove menu items.

**_Order Tracking:_** Monitor and manage incoming orders.

**_User Management:_** Handle user data and preferences.

**_Push Notifications:_** Keep staff informed about new orders and updates.

**_Sold Product Details:_** View detailed information about sold products.

# Screen Shots

![Screenshot_20240104_144504_com makaota mammamskitchenmanager_284x600_242x512](https://github.com/makaota/Mamma_Ms_Kitchen_Manager/assets/74915165/805545fb-92b7-46a8-ae58-260f58c9987f)
![Screenshot_20240104_144523_com makaota mammamskitchenmanager_284x600_242x512](https://github.com/makaota/Mamma_Ms_Kitchen_Manager/assets/74915165/b69b4108-8d37-44d2-aec1-c88439eebac0)
![Screenshot_20240104_144539_com makaota mammamskitchenmanager_284x600_242x512](https://github.com/makaota/Mamma_Ms_Kitchen_Manager/assets/74915165/6e196f36-88da-48b6-a42a-ace13fffbe47)

# Project Structure

# Models

**_SoldProduct:_** Model class for detailed information about sold products.

**_NotificationAPI:_** Interface for handling push notifications.

**_OpenCloseStore:_** Model for managing the open/close status of the store.

**_Product:_** Model for menu items.

**_Address:_** Model class for user addresses.

**_CartItem:_** Model class for items in the user's cart.

**_User:_** Model for user details.

# Firestore

**_FirestoreClass:_** Manages interactions with the Firestore database.

# Interfaces

**_MamaMsKitchenManager:_** Interface for managing operations related to Mama M's Kitchen.

# UI

# Activities

**_AddMenuActivity:_** Allows staff to add new menu items.

**_BaseActivity:_** Base activity with common functionalities.

**_DashboardActivity:_** Main activity for overview and navigation.

**_ForgotPasswordActivity:_** Enables password recovery for staff.

**_LoginActivity:_** Handles staff login.

**_MainActivity:_** Central activity for the manager's workflow.

**_MyOrderDetailsActivity.kt:_** Displays details of a specific order.

**_OpenCloseStoreActivity:_** Manages the store's open/close status.

**_ProductDetailsActivity:_** Displays details of a specific menu item.

**_RegisterActivity:_** Manages staff registration.

**_SettingsActivity:_** Allows staff to configure app settings.

**_SoldProductsDetailsActivity:_** Displays detailed information about sold products.

**_SplashActivity:_** Splash screen displayed on app launch.

**_UserProfileActivity:_** Displays and manages staff profiles.

# Adapters

**_CartItemsListAdapter:_** Adapter for displaying items in the cart.

**_MyOrdersListAdapter:_** Adapter for displaying manager's orders.

**_MyProductListAdapter:_** Adapter for displaying menu items.

**_SoldProductsListAdapter:_** Adapter for displaying sold products.

# Fragments

**_BaseFragment:_** Base fragment with common functionalities.

**_ManageMenuFragment:_** Fragment for managing menu items.

**_ManageOrdersFragment:_** Fragment for handling orders.

**_SoldProductsFragment:_** Fragment for managing sold products.

# Utils

**_Constants:_** Constants used throughout the app.

**_FirebaseService.kt:_** Service for handling Firebase functionalities.

**_GlideLoader:_** Utility for image loading using Glide.

**_RAMButton, RAMEditText, RAMRadioButton, RAMTextView, RAMTextViewBold:_** Custom UI components.

**_RetrofitInstance:_** Retrofit instance for API communication.

# System Requirements

**Supported Platforms**
  
- Android

**Minimum OS Version**
  
- Android 5.0 (Lollipop) and above
  
# Hardware Requirements

- Standard requirements for modern smartphones.
  
# Installation Guide

**Download and Installation Steps** 

**_Clone the repository:_** git clone https://github.com/your-username/mama-ms-kitchen-manager.git

Open the project in Android Studio or your preferred IDE.

Build and run the app on your emulator or physical device.

# Usage

**Launch the app.**

- Log in with your staff credentials.

- Navigate through the dashboard to manage menu items, orders, and other functionalities.
  
- Stay informed about new orders through push notifications.
  
# Contributing

-If you'd like to contribute to Mama M's Kitchen Manager App, please follow these steps:

- Fork the repository.
Create a new branch: git checkout -b feature/new-feature.
Commit your changes: git commit -m 'Add new feature'.
Push to the branch: git push origin feature/new-feature.
Submit a pull request.

# License

- This project is licensed under the MIT License.

# Contact

For any questions or support, please email us at sa.makaota@gmail.com.
