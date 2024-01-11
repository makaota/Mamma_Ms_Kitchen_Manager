
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

![Samsung Galaxy S9 Screenshot 0_384x768](https://github.com/makaota/Mamma_Ms_Kitchen_Manager/assets/74915165/9e161dbb-80d6-4209-aed5-815cc5e0b258)
![Samsung Galaxy S9 Screenshot 1_384x768](https://github.com/makaota/Mamma_Ms_Kitchen_Manager/assets/74915165/1c113149-7969-4a83-a987-a51c15348490)
![Samsung Galaxy S9 Screenshot 2_384x768](https://github.com/makaota/Mamma_Ms_Kitchen_Manager/assets/74915165/213c1302-dc75-426c-a380-d7f9e9dc203f)
![Samsung Galaxy S9 Screenshot 3_384x768](https://github.com/makaota/Mamma_Ms_Kitchen_Manager/assets/74915165/1ac84a35-9239-476d-bd90-f169b886812d)
![Samsung Galaxy S9 Screenshot 4_384x768](https://github.com/makaota/Mamma_Ms_Kitchen_Manager/assets/74915165/868d4c15-d5df-474e-965c-c00c791e3b79)
![Samsung Galaxy S9 Screenshot 5_384x768](https://github.com/makaota/Mamma_Ms_Kitchen_Manager/assets/74915165/416ce603-1ef8-40fd-a8aa-2698e195e4d9)
![Samsung Galaxy S9 Screenshot 6_384x768](https://github.com/makaota/Mamma_Ms_Kitchen_Manager/assets/74915165/69c5e172-336e-47eb-82aa-ba993811834c)
![Samsung Galaxy S9 Screenshot 7_384x768](https://github.com/makaota/Mamma_Ms_Kitchen_Manager/assets/74915165/1fefb960-035f-4978-aa27-3c5f61566ca0)
![Samsung Galaxy S9 Screenshot 8_384x768](https://github.com/makaota/Mamma_Ms_Kitchen_Manager/assets/74915165/c70cc5d3-80e0-4724-a3dc-bcdc0c0eb695)
![Samsung Galaxy S9 Screenshot 9_384x768](https://github.com/makaota/Mamma_Ms_Kitchen_Manager/assets/74915165/dded6380-8c79-4163-b221-3a360adde8a0)
![Samsung Galaxy S9 Screenshot 0_384x768](https://github.com/makaota/Mamma_Ms_Kitchen_Manager/assets/74915165/8b6b7a45-69c3-49eb-b1e8-29ea2421177d)
![Samsung Galaxy S9 Screenshot 0_384x768](https://github.com/makaota/Mamma_Ms_Kitchen_Manager/assets/74915165/4d88e5b4-2b1e-4a71-8b77-63650b47898e)
![Samsung Galaxy S9 Screenshot 2_384x768](https://github.com/makaota/Mamma_Ms_Kitchen_Manager/assets/74915165/0dc4a7df-c53e-4eca-8461-d6454f73ece4)
![Samsung Galaxy S9 Screenshot 3_384x768](https://github.com/makaota/Mamma_Ms_Kitchen_Manager/assets/74915165/e23fab9e-e69a-4cd2-a2e2-2717177a7307)
![Samsung Galaxy S9 Screenshot 4_384x768](https://github.com/makaota/Mamma_Ms_Kitchen_Manager/assets/74915165/d8c3d5a8-180f-4813-b262-0d7db1b67d63)
![Samsung Galaxy S9 Screenshot 5_384x768](https://github.com/makaota/Mamma_Ms_Kitchen_Manager/assets/74915165/81f93141-40e4-492d-8024-586e42be0cd3)


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
