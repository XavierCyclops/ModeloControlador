$(function () {
                    $(":file").change(function () {
                        if (this.files && this.files[0]) {
                            var reader = new FileReader();
                            reader.onload = imageIsLoaded;
                            reader.readAsDataURL(this.files[0]);
                        }
                    });
                });

                function imageIsLoaded(e) {
                    $('#perfil').attr('src', e.target.result);
                };


$( document ).ready(function() {
   $("[id$='guardar']").click( function(e) {
       
       var source = $('#perfil').attr('src');
       $('#fotoPrincipal').attr('src', source);
       PF('photo').hide();
   }); 
});

