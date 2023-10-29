var globalTimeouts = null;
function analizar() {
   
    $("#divGeneral").addClass("d-none");
    $("#divCargando").removeClass("d-none");

    return true;
   }

   let inputText = document.getElementById("inputText");
   let input1 = document.getElementById("input1");
   let input2 = document.getElementById("input2");

   inputText.addEventListener("input", function(event) {
    console.log("hola");
      input1.value = inputText.value;
      input2.value = inputText.value;
   });

   window.onload = function() {
   

  
    if (input1.value !== "") {
        inputText.value = input1.value;
    } else {
        
        if (input2.value !== "") {
            inputText.value = input2.value;
        }
    }
};