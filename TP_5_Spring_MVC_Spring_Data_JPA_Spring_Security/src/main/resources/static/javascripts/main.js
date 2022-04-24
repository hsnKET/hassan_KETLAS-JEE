let deleteEtu = document.getElementById('deleteEtu');
console.log("running....")
// deleteEtu.addEventListener("click",ev => {
//     ev.preventDefault();
//     deleteEtudiant()
// })
function  deleteEtudiant(e){
    // let id = document.getElementById("deleteEtu").getAttribute("id");
    e.preventDefault();
    let id = e.target.getAttribute("id");
    let p = e.target.getAttribute("p");
    let size = e.target.getAttribute("size");
    let q = e.target.getAttribute("q");
    console.log("id : "+id)
    fetch("http://localhost:8080/admin/etudiants/delete?id="+id+"&p="+p+"&q="+q+"&size="+size, {

        // Adding method type
        method: "DELETE",

        // Adding body or contents to send
        body: JSON.stringify({

        }),

        // Adding headers to the request
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
    })

        // Converting to JSON
        .then(response => response.json())

        // Displaying results to console
        .then(json =>{
            console.log(json)
            console.log(p+":"+q+":"+size)
         window.location.replace("http://localhost:8080/user/etudiants?p="+p+"&q="+"&size="+size);
        }).catch(reason => console.log(reason));
}