window.onload = function() {
    console.log("liff init start");
    const defaultLiffId = "liffId";
    registerButtonHandlers();
    liff.init({
        liffId: defaultLiffId,
        withLoginOnExternalBrowser: true
    })
    .then(() => {
        console.log("liff init success");
    });
};

/**
* Register event handlers for the buttons displayed in the app
*/
function registerButtonHandlers() {
    // get access token
    document.getElementById('getAccessToken').addEventListener('click', function() {
        if (!liff.isLoggedIn() && !liff.isInClient()) {
            alert('To get an access token, you need to be logged in. Please tap the "login" button below and try again.');
        } else {
            const accessToken = liff.getAccessToken();
            axios.get("{change your domain}/liff/getLineId",{
                headers: {
                    'Authorization': 'Bearer ' + accessToken
                }
            })
            .then(function(response){
                const userId = response.data;
                alert(userId);
            });
        }
    });
}
