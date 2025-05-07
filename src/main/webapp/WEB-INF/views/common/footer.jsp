</div>
    </div>
    <footer style="background: linear-gradient(to right, #2c3e50, #34495e); color: #ecf0f1; text-align: center; padding: 1.5rem 0; margin-top: 2rem; box-shadow: 0 -3px 10px rgba(0,0,0,0.1);">
        <div class="container">
            <p>&copy; 2023 BookVerse Management System | All Rights Reserved</p>
            <small>Designed with <span style="color: #e74c3c;">♥</span> for Literature Enthusiasts</small>
        </div>
    </footer>
    <script>
        // Simple animation for alerts
        document.addEventListener('DOMContentLoaded', function() {
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(alert => {
                setTimeout(() => {
                    alert.style.opacity = '0';
                    alert.style.transition = 'opacity 0.5s ease';
                    setTimeout(() => {
                        alert.style.display = 'none';
                    }, 500);
                }, 5000);
            });
        });
    </script>
</body>
</html>