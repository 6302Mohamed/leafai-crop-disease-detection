# LeafAI – Offline Mobile Crop Disease Detection (Android + TensorFlow Lite)

LeafAI is a mobile application for crop disease detection running directly on Android devices using TensorFlow Lite.  
It is designed for agricultural environments with unreliable connectivity, enabling farmers to receive disease diagnosis and treatment guidance without internet access.

The application additionally integrates weather data to support contextual decision-making in disease risk.

---

## Purpose

Smallholder farmers often struggle to access timely agronomy services. Crop diseases frequently go undiagnosed until they cause significant yield loss.  
LeafAI provides an on-device, real-time diagnostic tool to assist farmers in identifying crop diseases early and applying appropriate interventions.

---

## Core Capabilities

- **Offline disease detection** using TensorFlow Lite models
- **CameraX-based image capture** for leaf imaging
- **Weather information** displayed alongside predictions for environmental context
- **Actionable output** including:
    - Disease classification with confidence score
    - Symptoms and biological causes
    - Recommended treatment and prevention methods
- Support for multiple commercial crops:
    - Cotton
    - Tea
    - Coffee
- Modular architecture enabling expansion to additional crops and disease types

---

## Machine Learning Overview

LeafAI uses lightweight convolutional neural networks optimized for mobile inference.

- Training environment: **Python / TensorFlow / Keras on Google Colab**
- Data pipeline: **NumPy, Pandas**,
- Visualization and evaluation: **Matplotlib, Seaborn**
- Deeplearning Algorithm:MobileVNet
- Export pipeline: TensorFlow Lite conversion
- Inference: On-device using NNAPI for performance

Models are trained from curated datasets, validated for robustness against lighting and background variations, and deployed as `.tflite` files.

---

## Technical Stack

### Mobile Application
- Kotlin (Android)
- CameraX for real-time capture
- Material Design UI components
- REST retrieval of environmental weather context

### Machine Learning
- TensorFlow / Keras
- Python on Google Colab
- Pandas, NumPy for preprocessing
- Matplotlib for evaluation
- Data augmentation for generalization

---

## Project Structure

