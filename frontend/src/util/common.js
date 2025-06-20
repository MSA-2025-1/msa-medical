import router from "@/router/index.js";

export const common =  {

    historyBack : () => {
        return router.push({name: 'home'});
    }

}