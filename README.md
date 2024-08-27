When configure Jenkins, remember to configure swap space, it enhance its build performance, reduce building time and avoid failure build.
Step to configure swap space:

1. check swap space: free -m
2. command line:
  sudo fallocate -l 1G /swapfile

  sudo chmod 600 /swapfile
  
  sudo mkswap /swapfile
  
  sudo swapon /swapfile
  

Source: https://stackoverflow.com/questions/31041512/jenkins-build-throwing-an-out-of-memory-error/42521447
https://www.digitalocean.com/community/tutorials/how-to-add-swap-space-on-ubuntu-22-04
