# Note1 NewStart #

## 1. Install VS code ##

## 2. Markdown ##

Choose to use VS code to edit markdown files.

Use [Marp for VS Code](https://marketplace.visualstudio.com/items?itemName=marp-team.marp-vscode) as ppt tool.

Use [Markdownlint](https://marketplace.visualstudio.com/items?itemName=DavidAnson.vscode-markdownlint) as a lint.

Use [vscode-pandoc](https://marketplace.visualstudio.com/items?itemName=DougFinke.vscode-pandoc) to export html, doc and pdf. Note that it's just a plugin, and an extra [Pandoc](https://pan.baidu.com/s/1slawmr7) is still needed.

Start from [a brief instruction of Markdown](https://mazhuang.org/2018/09/06/markdown-intro/ "A brief instruction of Markdown").

## 3. About Workspaces and folders ##

In VS Code, Workspaces and ffolders are different concepts.

Workspaces determine what plugins is loaded - avoid useless loading. Workspaces information can be saved as files. Folders are still folders, contain files.

I choose to save save workspace information on computer and put folders and files on a flash disk.

### Notice: How to add a new workspace ###

File-Close Workspace-File-Save Workspace as.

## 4. Github ##

Start froom [this](https://www.liaoxuefeng.com/wiki/896043488029600/896202815778784).

So, it's Linus who used two weeks to write a distributed revision control in C, which is Git.

Get github from: [https://github.com/waylau/git-for-win](https://github.com/waylau/git-for-win).
After installation, search git and run Git Bash to have some basical settings:

```git
Username@ComputerName MINGW64 ~
$ git config --global user.name "Your Name"
$ git config --global user.email "email@example.com"
```

Then create a repository and init Git:

```git
Username@ComputerName MINGW64 ~
$ mkdir learngit
$ cd learngit
$ pwd
/c/Users/Lenovo/learngit
$ git init
Initialized empty Git repository in /Users/michael/learngit/.git/
```

Note for windows that if you don't want to make your repository in \c, then right click GitBash-priority window-delet"--ed--home--" in target textbox, and change the path to whatever you want in Starting Position textbox . ([Reference](https://blog.csdn.net/nima1994/article/details/51960101))

Then your GitBash will be like this:

```git
Username@ComputerName MINGW64 YourPath
$
```

And mine as an example:

```git
Lenovo@Lenovo-PC MINGW64 /e
$ mkdir Repository

Lenovo@Lenovo-PC MINGW64 /e
$ cd Repository

Lenovo@Lenovo-PC MINGW64 /e/Repository
$ git init
Initialized empty Git repository in E:/Repository/.git/

Lenovo@Lenovo-PC MINGW64 /e/Repository (master)
$
```

Add a file into repository:

```git
Lenovo@Lenovo-PC MINGW64 /e
$ git add pathOfYourFile

Lenovo@Lenovo-PC MINGW64 /e/Repository (master)
$ git commit -m "Move Note1 into repository"
[master (root-commit) 16ed1d5] Move Note1 into repository
 1 file changed, 35 insertions(+)
 create mode 100644 test/Note1.md

