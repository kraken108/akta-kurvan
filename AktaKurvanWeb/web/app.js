/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


(function () {
    const config = {
        apiKey: "AIzaSyCd-TDXZEj0_E8S-KGfDzonMLPZdgucsZ8",
        authDomain: "mobilapp-67b55.firebaseapp.com",
        databaseURL: "https://mobilapp-67b55.firebaseio.com",
        projectId: "mobilapp-67b55",
        storageBucket: "mobilapp-67b55.appspot.com",
        messagingSenderId: "777985001299"
    };
    firebase.initializeApp(config);
    
    const txtEmail = document.getElementById('txtEmail');
    const txtPassword = document.getElementById('txtPassword');
    const btnLogin = document.getElementById('btnLogin');
    
    btnLogin.addEventListener('click', e=> {
        alert('öh');
       const email = txtEmail.value;
       const pass = txtPassword.value;
       const auth = firebase.auth();
       
       const promise = auth.signInWithEmailAndPassword(email, pass);
       promise.catch(e => alert('error!'));
    });
}());