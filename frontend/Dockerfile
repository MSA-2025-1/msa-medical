FROM nginx:stable

COPY dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf

CMD ["nginx", "-g", "daemon off;"]