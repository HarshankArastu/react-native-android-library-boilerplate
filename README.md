## React Native Android Bonjour Library
This project is converted bridge of bonjour RDNSS lib for React Native easy to import and use

## Getting started

## Installing it as a library in your main project
There are many ways to do this, here's the way I do it:

1. Push it to **GitHub**.
2. Do `npm install --save git+https://github.com/HarshankArastu/react-native-android-library-boilerplate.git` in your main project.
3. Link the library:
    * Add the following to `android/settings.gradle`:
        ```
        include ':react-native-android-library-boilerplate'
        project(':react-native-android-library-boilerplate').projectDir = new File(settingsDir, '../node_modules/react-native-android-library-boilerplate/android')
        ```

    * Add the following to `android/app/build.gradle`:
        ```xml
        ...

        dependencies {
            ...
            compile project(':react-native-android-library-boilerplate')
        }
        ```
4. Simply `import/require` it by the name defined in your library's `package.json`:

    ```javascript

    import Boilerplate from 'react-native-android-library-boilerplate'

-------------------------------
    
			constructor(props){
        			super(props)
        			this.state = {
            				resultArray:[]
        			}
        			Boilerplate.getData();
    			}

--------------------------------

			async componentDidMount(){

    				this.setState({
        				resultArray : await Boilerplate.getBonjourDevicesList(),
    				})
    				

			}
    ```
5. You can test and develop your library by importing the `node_modules` library into **Android Studio** if you don't want to install it from _git_ all the time.# react-native-android-library-boilerplate
