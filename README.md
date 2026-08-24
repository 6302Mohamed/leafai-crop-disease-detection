# LeafAI – Offline Mobile Crop Disease Detection

## Android + TensorFlow Lite

<div align="center">

[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge\&logo=android\&logoColor=white)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge\&logo=kotlin\&logoColor=white)](https://kotlinlang.org/)
[![TensorFlow](https://img.shields.io/badge/TensorFlow-FF6F00?style=for-the-badge\&logo=tensorflow\&logoColor=white)](https://www.tensorflow.org/)
[![Python](https://img.shields.io/badge/Python-3776AB?style=for-the-badge\&logo=python\&logoColor=white)](https://www.python.org/)
[![Google Colab](https://img.shields.io/badge/Colab-F9AB00?style=for-the-badge\&logo=googlecolab\&logoColor=white)](https://colab.research.google.com/)

*An offline-first Android application that uses on-device computer vision and environmental context to support early crop disease detection.*

</div>

---

> **Final Year Project:** LeafAI was independently developed over an intensive **two-month period**, covering the full development lifecycle from concept design and dataset curation to model training, Android application development, integration, and testing.

---

## Overview

Smallholder farmers often have limited access to timely agricultural and agronomy services. As a result, crop diseases may remain undiagnosed until they cause significant damage and yield loss.

**LeafAI** addresses this challenge by providing an Android-based crop disease detection system capable of performing image classification directly on the user's device.

The application uses optimized **TensorFlow Lite** models to analyze crop leaf images without requiring an internet connection for disease detection. This makes the system suitable for rural and low-connectivity environments.

In addition to image-based diagnosis, LeafAI integrates live weather information to provide environmental context that may help users better understand conditions associated with crop disease development.

---

## Core Features

* **Offline Disease Detection**
  Crop disease classification is performed directly on the Android device using optimized TensorFlow Lite models.

* **CameraX Integration**
  Supports efficient image capture and leaf photography through Android's CameraX API.

* **Environmental Context**
  Displays live weather information alongside disease detection results when network connectivity is available.

* **Detailed Disease Information**
  Detection results include:

  * Predicted disease class
  * Model confidence score
  * Common symptoms
  * Possible biological causes
  * Recommended treatment approaches
  * Preventive measures

* **Multi-Crop Support**
  Separate machine learning models are provided for:

  * Cotton
  * Tea
  * Robusta Coffee
  * Arabica Coffee

* **Modular Design**
  The architecture allows additional crops, disease classes, and TensorFlow Lite models to be integrated in the future.

---

## How It Works

1. The user selects their location and crop type.
2. A crop leaf image is captured or selected.
3. The image is preprocessed on the Android device.
4. The crop-specific TensorFlow Lite model performs inference locally.
5. The application displays the predicted disease and confidence score.
6. Disease symptoms, causes, treatment, and prevention information are shown.
7. When internet access is available, weather information is retrieved to provide additional environmental context.

> **Note:** Disease detection itself is designed to work offline. Internet connectivity is only required for features that depend on live external data, such as weather information.

---

## Machine Learning Pipeline

LeafAI uses lightweight convolutional neural networks optimized for mobile inference.

### Training Environment

* Python
* TensorFlow
* Keras
* Google Colab with GPU acceleration

### Data Processing

The training pipeline includes:

* Dataset cleaning and organization
* Image resizing and normalization
* Data augmentation
* Rotation
* Brightness variation
* Zoom
* Horizontal and vertical transformations where appropriate
* Training and validation dataset separation

### Model Architecture

The project uses **MobileNet-based transfer learning** to balance classification accuracy with the computational requirements of mobile devices.

Transfer learning enables the models to reuse visual features learned from large-scale image datasets while adapting the final classification layers to crop-specific disease categories.

### Evaluation

Model performance was evaluated using:

* Training and validation accuracy
* Training and validation loss
* Confusion matrices
* Per-class prediction analysis
* Visual performance plots

### Mobile Deployment

After training, each model was converted to the TensorFlow Lite (`.tflite`) format for deployment within the Android application.

Inference is performed directly on-device, with Android hardware acceleration support where available.

---

## Technical Stack

### Android Application

| Technology          | Purpose                               |
| ------------------- | ------------------------------------- |
| **Kotlin**          | Primary Android development language  |
| **Android Studio**  | Application development environment   |
| **TensorFlow Lite** | On-device machine learning inference  |
| **CameraX**         | Camera capture and image acquisition  |
| **Material Design** | User interface design                 |
| **REST API**        | Retrieval of live weather information |

### Machine Learning and Data

| Technology             | Purpose                                        |
| ---------------------- | ---------------------------------------------- |
| **Python**             | Model development and experimentation          |
| **TensorFlow / Keras** | Deep learning and transfer learning            |
| **NumPy**              | Numerical processing                           |
| **Pandas**             | Dataset manipulation and analysis              |
| **Matplotlib**         | Training and evaluation visualization          |
| **Seaborn**            | Statistical and confusion-matrix visualization |
| **Google Colab**       | GPU-based model training environment           |

---

## Screenshots

### Onboarding Flow

| Splash                                                     | Country                                                    | Region                                                     | Crop                                                       |
| ---------------------------------------------------------- | ---------------------------------------------------------- | ---------------------------------------------------------- | ---------------------------------------------------------- |
| ![](docs/Screenshots/Screenshot%202025-12-09%20022259.png) | ![](docs/Screenshots/Screenshot%202025-12-09%20022309.png) | ![](docs/Screenshots/Screenshot%202025-12-09%20022322.png) | ![](docs/Screenshots/Screenshot%202025-12-09%20022337.png) |

---

### Detection Workflow

| Main Screen                                                | Photo Selection                                            | Loaded Image                                               | Weather Context                                            |
| ---------------------------------------------------------- | ---------------------------------------------------------- | ---------------------------------------------------------- | ---------------------------------------------------------- |
| ![](docs/Screenshots/Screenshot%202025-12-09%20022348.png) | ![](docs/Screenshots/Screenshot%202025-12-09%20022359.png) | ![](docs/Screenshots/Screenshot%202025-12-09%20022408.png) | ![](docs/Screenshots/Screenshot%202025-12-09%20022431.png) |

---

### Disease Detection Result

| Full Detection Result                                      |
| ---------------------------------------------------------- |
| ![](docs/Screenshots/Screenshot%202025-12-09%20022449.png) |

---

## Dataset Summary and Model Performance

LeafAI uses curated agricultural image datasets from multiple public sources. A separate model was trained for each supported crop to improve crop-specific classification performance.

| Crop                 | Classes | Dataset Source                      | Validation Accuracy | Notes                                                              |
| -------------------- | ------- | ----------------------------------- | ------------------: | ------------------------------------------------------------------ |
| **Cotton**           | 4       | Kaggle – Cotton Leaf Disease        |                ~98% | Strong class separation produced consistent validation performance |
| **Tea**              | 8       | Roboflow Public Dataset             |                ~95% | Variation in lighting and viewing angles improves model robustness |
| **Coffee (Robusta)** | 3       | ReCoLe – DatasetNinja               |                ~95% | Field-image variation was handled using augmentation               |
| **Coffee (Arabica)** | 5       | Mendeley Data – Arabica Leaf Images |                ~94% | More difficult class boundaries and mild class imbalance           |

### Cotton Classes

* Bacterial Blight
* Curl Virus
* Fusarium Wilt
* Healthy

### Tea Classes

* Algal Leaf Spot
* Anthracnose
* Bird Eye Spot
* Brown Blight
* Gray Blight
* Healthy
* Red Leaf Spot
* White Spot

### Robusta Coffee Classes

* Healthy
* Coffee Red Spider Mite
* Coffee Rust

### Arabica Coffee Classes

* Healthy
* Leaf Rust
* Leaf Miner
* Phoma
* Cercospora

> Validation accuracy represents performance on the validation datasets used during model development. Real-world accuracy may vary depending on image quality, lighting, leaf orientation, disease severity, and environmental conditions.

---

## Project Structure

```text
leafai-crop-disease-detection/
├── app/                     # Android application source code
├── docs/
│   └── Screenshots/         # Screenshots displayed in this README
├── build.gradle.kts         # Project-level Gradle configuration
├── settings.gradle.kts      # Gradle project settings
└── README.md                # Project documentation
```

---

## Offline-First Architecture

One of LeafAI's primary design goals is to minimize dependence on network connectivity.

The machine learning workflow runs locally:

```text
Leaf Image
    │
    ▼
Image Preprocessing
    │
    ▼
Crop-Specific TensorFlow Lite Model
    │
    ▼
Disease Classification
    │
    ▼
Confidence Score
    │
    ▼
Symptoms + Causes + Treatment + Prevention
```

This approach provides several advantages:

* Reduced dependence on mobile connectivity
* Faster prediction response times
* No requirement to upload leaf images to a remote inference server
* Better suitability for rural environments
* Reduced server-side infrastructure requirements

Weather information remains an online enhancement and does not prevent the core disease-classification functionality from operating offline.

---

## Supported Crops

| Crop           | Model           | Number of Classes |
| -------------- | --------------- | ----------------: |
| Cotton         | TensorFlow Lite |                 4 |
| Tea            | TensorFlow Lite |                 8 |
| Robusta Coffee | TensorFlow Lite |                 3 |
| Arabica Coffee | TensorFlow Lite |                 5 |

Each crop uses a dedicated model rather than a single combined classifier.

---

## Design Goals

LeafAI was designed around four primary goals:

1. **Accessibility** – Make crop disease identification available on commonly accessible Android devices.
2. **Offline Functionality** – Ensure disease classification does not depend on continuous internet connectivity.
3. **Efficiency** – Use lightweight models suitable for mobile inference.
4. **Extensibility** – Allow new crops and disease classes to be added without redesigning the entire application.

---

## Limitations

LeafAI is an image-classification system and should not be considered a replacement for professional agricultural diagnosis.

Prediction quality may be affected by:

* Poor lighting
* Blurry images
* Multiple leaves or objects in the frame
* Partially visible symptoms
* Early-stage infections
* Visually similar diseases
* Images that differ substantially from the training datasets

The application should therefore be treated as a **decision-support tool** rather than a definitive diagnostic system.

---

## Future Improvements

Potential future development includes:

* Support for additional crop species
* Larger and more geographically diverse datasets
* Improved model quantization and inference optimization
* Automatic leaf detection and segmentation
* Disease severity estimation
* Multi-language support
* Improved accessibility for rural users
* Historical diagnosis tracking
* Location-based disease-risk analysis
* Expanded environmental risk indicators
* Model performance benchmarking across different Android devices

---

## Project Scope

This project demonstrates the complete workflow required to move a machine learning solution from experimentation to mobile deployment:

```text
Dataset Collection
        ↓
Data Preparation
        ↓
Model Training
        ↓
Model Evaluation
        ↓
TensorFlow Lite Conversion
        ↓
Android Integration
        ↓
On-Device Inference
        ↓
User-Facing Disease Diagnosis
```

LeafAI combines **mobile development, computer vision, deep learning, and agricultural technology** into a single offline-first application.

---

## Disclaimer

LeafAI provides machine learning-based predictions for informational and educational purposes.

Disease classifications and treatment information should not be treated as a substitute for advice from qualified agricultural professionals, plant pathologists, or local extension services. Treatment recommendations should be verified against locally approved agricultural practices and regulations before application.

---
## Usage & Contributions

Whether you are a developer, student, researcher, or simply exploring the repository, you are welcome to use, fork, and modify this project for learning, experimentation, and agricultural technology research.

Contributions and improvements are also welcome. Possible areas for expansion include:

* Adding support for additional crops and disease classes
* Improving model accuracy and efficiency
* Expanding or diversifying the training datasets
* Enhancing the Android user interface and user experience
* Improving TensorFlow Lite inference performance
* Adding localization and multi-language support
* Extending environmental and weather-based disease-risk features

Feel free to adapt the project to your own use case and build upon the existing implementation.

---

## Author

**Mohamed Yousuf Hussein**

Developed with passion and completed as a **Final Year Project**, covering the full process from dataset preparation and machine learning model development to Android integration and on-device deployment.

---

<div align="center">

### LeafAI

**Offline crop disease detection powered by Android and TensorFlow Lite.**

</div>
